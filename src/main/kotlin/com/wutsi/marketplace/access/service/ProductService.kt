package com.wutsi.marketplace.access.service

import com.wutsi.marketplace.access.dao.ProductRepository
import com.wutsi.marketplace.access.dto.CheckProductAvailabilityRequest
import com.wutsi.marketplace.access.dto.CreateProductRequest
import com.wutsi.marketplace.access.dto.Product
import com.wutsi.marketplace.access.dto.ProductSummary
import com.wutsi.marketplace.access.dto.SearchProductRequest
import com.wutsi.marketplace.access.dto.UpdateProductAttributeRequest
import com.wutsi.marketplace.access.dto.UpdateProductStatusRequest
import com.wutsi.marketplace.access.entity.PictureEntity
import com.wutsi.marketplace.access.entity.ProductEntity
import com.wutsi.marketplace.access.entity.ReservationEntity
import com.wutsi.marketplace.access.enums.ProductSort
import com.wutsi.marketplace.access.enums.ProductStatus
import com.wutsi.marketplace.access.enums.StoreStatus
import com.wutsi.marketplace.access.error.ErrorURN
import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType
import com.wutsi.platform.core.error.exception.BadRequestException
import com.wutsi.platform.core.error.exception.ConflictException
import com.wutsi.platform.core.error.exception.NotFoundException
import org.springframework.stereotype.Service
import java.time.ZoneOffset
import java.util.Date
import javax.persistence.EntityManager
import javax.persistence.Query

@Service
class ProductService(
    private val dao: ProductRepository,
    private val categoryService: CategoryService,
    private val pictureService: PictureService,
    private val storeService: StoreService,
    private val em: EntityManager
) {
    fun create(request: CreateProductRequest): ProductEntity {
        val store = storeService.findById(request.storeId)
        val product = dao.save(
            ProductEntity(
                title = request.title,
                summary = request.summary,
                status = ProductStatus.DRAFT,
                price = request.price,
                category = request.categoryId?.let { categoryService.findById(it) },
                store = store,
                currency = store.currency,
                quantity = request.quantity
            )
        )

        if (!request.pictureUrl.isNullOrEmpty()) {
            product.thumbnail = pictureService.create(product, request.pictureUrl)
            dao.save(product)
        }

        storeService.updateProductCount(store)
        return product
    }

    fun delete(id: Long) {
        val product = findById(id)
        product.isDeleted = true
        product.deleted = Date()
        dao.save(product)

        storeService.updateProductCount(product.store)
    }

    fun findById(id: Long): ProductEntity {
        val product = dao.findById(id)
            .orElseThrow {
                NotFoundException(
                    error = Error(
                        code = ErrorURN.PRODUCT_NOT_FOUND.urn,
                        parameter = Parameter(
                            name = "id",
                            value = id,
                            type = ParameterType.PARAMETER_TYPE_PATH
                        )
                    )
                )
            }

        if (product.isDeleted) {
            throw NotFoundException(
                error = Error(
                    code = ErrorURN.PRODUCT_DELETED.urn,
                    parameter = Parameter(
                        name = "id",
                        value = id,
                        type = ParameterType.PARAMETER_TYPE_PATH
                    )
                )
            )
        }

        return product
    }

    fun toProduct(product: ProductEntity, language: String?) = Product(
        id = product.id ?: -1,
        title = product.title ?: "",
        summary = product.summary,
        price = product.price,
        comparablePrice = product.comparablePrice,
        currency = product.currency,
        status = product.status.name,
        storeId = product.store.id ?: -1,
        category = product.category?.let { categoryService.toCategorySummary(it, language) },
        created = product.created.toInstant().atOffset(ZoneOffset.UTC),
        updated = product.updated.toInstant().atOffset(ZoneOffset.UTC),
        published = product.published?.toInstant()?.atOffset(ZoneOffset.UTC),
        description = product.description,
        quantity = product.quantity,
        thumbnail = product.thumbnail?.let { pictureService.toPictureSummary(it) },
        pictures = product.pictures
            .filter { !it.isDeleted }
            .map { pictureService.toPictureSummary(it) }
    )

    fun toProductSummary(product: ProductEntity, language: String?) = ProductSummary(
        id = product.id ?: -1,
        title = product.title ?: "",
        summary = product.summary,
        price = product.price,
        comparablePrice = product.comparablePrice,
        currency = product.currency,
        status = product.status.name,
        storeId = product.store.id ?: -1,
        categoryId = product.category?.id,
        created = product.created.toInstant().atOffset(ZoneOffset.UTC),
        updated = product.updated.toInstant().atOffset(ZoneOffset.UTC),
        quantity = product.quantity,
        thumbnailUrl = product.thumbnail?.url
    )

    fun updateAttribute(id: Long, request: UpdateProductAttributeRequest) {
        val product = findById(id)
        updateAttribute(product, request)
    }

    fun updateAttribute(product: ProductEntity, request: UpdateProductAttributeRequest) {
        when (request.name.lowercase()) {
            "title" -> product.title = toString(request.value) ?: "NO TITLE"
            "summary" -> product.summary = toString(request.value)
            "description" -> product.description = toString(request.value)
            "price" -> product.price = toLong(request.value)
            "comparable-price" -> product.comparablePrice = toLong(request.value)
            "thumbnail-id" -> product.thumbnail = toLong(request.value)?.let { pictureService.findById(it) }
            "category-id" -> product.category = toLong(request.value)?.let { categoryService.findById(it) }
            "quantity" -> product.quantity = toInt(request.value) ?: 0
            else -> throw BadRequestException(
                error = Error(
                    code = ErrorURN.ATTRIBUTE_NOT_VALID.urn,
                    parameter = Parameter(
                        name = "name",
                        value = request.name,
                        type = ParameterType.PARAMETER_TYPE_PAYLOAD
                    )
                )
            )
        }
        dao.save(product)
    }

    fun setThumbnail(product: ProductEntity, picture: PictureEntity?) {
        product.thumbnail = picture
        dao.save(product)
    }

    fun updateStatus(id: Long, request: UpdateProductStatusRequest) {
        val product = findById(id)
        val status = ProductStatus.valueOf(request.status.uppercase())
        if (status == product.status) {
            return
        }

        when (status) {
            ProductStatus.DRAFT -> product.published = null
            ProductStatus.PUBLISHED -> product.published = Date()

            else -> throw BadRequestException(
                error = Error(
                    code = ErrorURN.STATUS_NOT_VALID.urn,
                    parameter = Parameter(
                        name = "status",
                        value = request.status,
                        type = ParameterType.PARAMETER_TYPE_PAYLOAD
                    )
                )
            )
        }
        product.status = status
        dao.save(product)
        storeService.updateProductCount(product.store)
    }

    fun checkAvailability(request: CheckProductAvailabilityRequest) {
        val productMap = search(
            request = SearchProductRequest(
                productIds = request.items.map { it.productId },
                limit = request.items.size
            )
        ).associateBy { it.id }

        request.items.forEach {
            val product = productMap[it.productId]
                ?: throw availabilityException(it.productId)

            if (product.quantity != null && product.quantity!! < it.quantity) {
                throw availabilityException(it.productId, product.quantity)
            }
        }
    }

    fun updateQuantities(reservation: ReservationEntity) {
        val products = mutableListOf<ProductEntity>()
        reservation.items.forEach {
            val product = it.product
            if (product.quantity != null) {
                product.quantity = product.quantity!! - it.quantity
                if (product.quantity!! < 0) {
                    throw availabilityException(product.id!!, product.quantity)
                } else {
                    products.add(product)
                }
            }
        }

        if (products.isNotEmpty()) {
            dao.saveAll(products)
        }
    }

    private fun availabilityException(productId: Long, quantity: Int? = null) = ConflictException(
        error = Error(
            code = ErrorURN.PRODUCT_NOT_AVAILABLE.urn,
            data = mapOf(
                "product-id" to productId,
                "quantity" to (quantity ?: "")
            )
        )
    )

    fun search(request: SearchProductRequest): List<ProductEntity> {
        val sql = sql(request)
        val query = em.createQuery(sql)
        parameters(request, query)
        return query
            .setFirstResult(request.offset)
            .setMaxResults(request.limit)
            .resultList as List<ProductEntity>
    }

    private fun sql(request: SearchProductRequest): String {
        val select = select()
        val where = where(request)
        val orderBy = orderBy(request)
        return if (where.isNullOrEmpty()) {
            select
        } else {
            "$select WHERE $where ORDER BY $orderBy"
        }
    }

    private fun select(): String =
        "SELECT P FROM ProductEntity P"

    private fun where(request: SearchProductRequest): String {
        val criteria = mutableListOf("P.isDeleted=false") // Product not deleted
        criteria.add("P.store.status=:store_status") // Store status

        if (request.storeId != null) {
            criteria.add("P.storeId = :storeId")
        }

        if (request.productIds.isNotEmpty()) {
            criteria.add("P.id IN :product_ids")
        }

        if (request.categoryIds.isNotEmpty()) {
            criteria.add("P.category.id IN :category_ids")
        }

        if (!request.status.isNullOrEmpty()) {
            criteria.add("P.status=:status")
        }

        return criteria.joinToString(separator = " AND ")
    }

    private fun orderBy(request: SearchProductRequest): String =
        if (ProductSort.PRICE_DESC.name.equals(request.sortBy, true)) {
            "P.price DESC"
        } else if (ProductSort.PRICE_ASC.name.equals(request.sortBy, true)) {
            "P.price ASC"
        } else {
            "P.title"
        }

    private fun parameters(request: SearchProductRequest, query: Query) {
        query.setParameter("store_status", StoreStatus.ACTIVE)

        if (request.storeId != null) {
            query.setParameter("store_id", request.storeId)
        }

        if (request.productIds.isNotEmpty()) {
            query.setParameter("product_ids", request.productIds)
        }

        if (request.categoryIds.isNotEmpty()) {
            query.setParameter("category_ids", request.categoryIds)
        }

        if (!request.status.isNullOrEmpty()) {
            query.setParameter("status", ProductStatus.valueOf(request.status.uppercase()))
        }
    }

    private fun toString(value: String?): String? =
        if (value.isNullOrEmpty()) {
            null
        } else {
            value
        }

    private fun toLong(value: String?): Long? =
        if (value.isNullOrEmpty()) {
            null
        } else {
            value.toLong()
        }

    private fun toInt(value: String?): Int? =
        if (value.isNullOrEmpty()) {
            null
        } else {
            value.toInt()
        }
}
