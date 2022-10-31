package com.wutsi.marketplace.access.service

import com.wutsi.marketplace.access.dao.ProductRepository
import com.wutsi.marketplace.access.dto.CategorySummary
import com.wutsi.marketplace.access.dto.CreateProductRequest
import com.wutsi.marketplace.access.dto.PictureSummary
import com.wutsi.marketplace.access.dto.Product
import com.wutsi.marketplace.access.entity.ProductEntity
import com.wutsi.marketplace.access.enums.ProductStatus
import com.wutsi.marketplace.access.error.ErrorURN
import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType
import com.wutsi.platform.core.error.exception.NotFoundException
import org.springframework.stereotype.Service
import java.time.ZoneOffset
import java.util.Date

@Service
class ProductService(
    private val dao: ProductRepository,
    private val categoryService: CategoryService,
    private val pictureService: PictureService,
    private val storeService: StoreService
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
                currency = store.currency
            )
        )

        product.thumbnail = pictureService.create(product, request.pictureUrl)
        dao.save(product)

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
        category = product.category?.let { categoryService.toCategorySummary(it, language) }
            ?: CategorySummary(id = -1),
        created = product.created.toInstant().atOffset(ZoneOffset.UTC),
        updated = product.updated.toInstant().atOffset(ZoneOffset.UTC),
        published = product.published?.toInstant()?.atOffset(ZoneOffset.UTC),
        description = product.description,
        quantity = product.quantity,
        thumbnail = product.thumbnail?.let { pictureService.toPictureSummary(it) } ?: PictureSummary(id = -1),
        pictures = product.pictures
            .filter { !it.isDeleted }
            .map { pictureService.toPictureSummary(it) }
    )
}
