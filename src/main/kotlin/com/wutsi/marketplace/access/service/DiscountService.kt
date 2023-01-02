package com.wutsi.marketplace.access.service

import com.wutsi.marketplace.access.dao.DiscountRepository
import com.wutsi.marketplace.access.dto.CreateDiscountRequest
import com.wutsi.marketplace.access.dto.Discount
import com.wutsi.marketplace.access.dto.DiscountSummary
import com.wutsi.marketplace.access.dto.SearchDiscountRequest
import com.wutsi.marketplace.access.dto.UpdateDiscountRequest
import com.wutsi.marketplace.access.entity.DiscountEntity
import com.wutsi.marketplace.access.error.ErrorURN
import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType
import com.wutsi.platform.core.error.exception.NotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.Date
import javax.persistence.EntityManager
import javax.persistence.Query

@Service
class DiscountService(
    private val dao: DiscountRepository,
    private val storeService: StoreService,
    private val em: EntityManager,
) {
    fun create(request: CreateDiscountRequest): DiscountEntity =
        dao.save(
            DiscountEntity(
                store = storeService.findById(request.storeId),
                starts = Date(request.starts.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()),
                ends = Date(request.ends.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()),
                name = request.name,
                rate = request.rate,
                allProducts = request.allProducts,
                created = Date(),
                updated = Date(),
            ),
        )

    fun update(id: Long, request: UpdateDiscountRequest) {
        val discount = findById(id)
        discount.name = request.name
        discount.starts = Date(request.starts.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli())
        discount.ends = Date(request.ends.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli())
        discount.rate = request.rate
        discount.allProducts = request.allProducts
        dao.save(discount)
    }

    fun delete(id: Long) {
        val opt = dao.findById(id)
        if (opt.isPresent) {
            val discount = opt.get()
            if (!discount.isDeleted) {
                discount.isDeleted = true
                discount.deleted = Date()
                discount.updated = Date()
                dao.save(discount)
            }
        }
    }

    fun findById(id: Long): DiscountEntity {
        val discount = dao.findById(id)
            .orElseThrow {
                NotFoundException(
                    error = Error(
                        code = ErrorURN.DISCOUNT_NOT_FOUND.urn,
                        parameter = Parameter(
                            name = "id",
                            value = id,
                            type = ParameterType.PARAMETER_TYPE_PATH,
                        ),
                    ),
                )
            }
        if (discount.isDeleted) {
            throw NotFoundException(
                error = Error(
                    code = ErrorURN.DISCOUNT_DELETED.urn,
                    parameter = Parameter(
                        name = "id",
                        value = id,
                        type = ParameterType.PARAMETER_TYPE_PATH,
                    ),
                ),
            )
        }
        return discount
    }

    fun toDiscount(discount: DiscountEntity) = Discount(
        id = discount.id,
        name = discount.name,
        storeId = discount.store.id ?: -1,
        starts = LocalDate.ofInstant(discount.starts.toInstant(), ZoneId.of("UTC")),
        ends = LocalDate.ofInstant(discount.ends.toInstant(), ZoneId.of("UTC")),
        rate = discount.rate,
        allProducts = discount.allProducts,
        productIds = discount.products.filter { !it.isDeleted }.mapNotNull { it.id },
        created = OffsetDateTime.ofInstant(discount.created.toInstant(), ZoneId.of("UTC")),
        updated = OffsetDateTime.ofInstant(discount.updated.toInstant(), ZoneId.of("UTC")),
    )

    fun toDiscountSummary(discount: DiscountEntity) = DiscountSummary(
        id = discount.id,
        name = discount.name,
        storeId = discount.store.id ?: -1,
        starts = LocalDate.ofInstant(discount.starts.toInstant(), ZoneId.of("UTC")),
        ends = LocalDate.ofInstant(discount.ends.toInstant(), ZoneId.of("UTC")),
        rate = discount.rate,
        created = OffsetDateTime.ofInstant(discount.created.toInstant(), ZoneId.of("UTC")),
    )

    fun search(request: SearchDiscountRequest): List<DiscountEntity> {
        val sql = sql(request)
        val query = em.createQuery(sql)
        parameters(request, query)
        return query
            .setFirstResult(request.offset)
            .setMaxResults(request.limit)
            .resultList as List<DiscountEntity>
    }

    private fun sql(request: SearchDiscountRequest): String {
        val select = select(request)
        val where = where(request)
        return if (where.isEmpty()) {
            select
        } else {
            "$select WHERE $where"
        }
    }

    private fun select(request: SearchDiscountRequest): String =
        if (request.productIds.isEmpty()) {
            "SELECT D FROM DiscountEntity D"
        } else {
            "SELECT D FROM DiscountEntity D LEFT JOIN D.products P"
        }

    private fun where(request: SearchDiscountRequest): String {
        val criteria = mutableListOf("D.isDeleted=false") // Discount not deleted

        if (request.storeId != null) {
            criteria.add("D.store.id = :store_id")
        }

        if (request.productIds.isNotEmpty()) {
            criteria.add("(D.allProducts=true OR P.id IN :product_ids)")
        }

        if (request.date != null) {
            criteria.add("D.starts <= :date AND D.ends >= :date")
        }

        return criteria.joinToString(separator = " AND ")
    }

    private fun parameters(request: SearchDiscountRequest, query: Query) {
        if (request.storeId != null) {
            query.setParameter("store_id", request.storeId)
        }

        if (request.productIds.isNotEmpty()) {
            query.setParameter("product_ids", request.productIds)
        }

        if (request.date != null) {
            query.setParameter("date", Date.from(request.date.atStartOfDay(ZoneId.of("UTC")).toInstant()))
        }
    }
}
