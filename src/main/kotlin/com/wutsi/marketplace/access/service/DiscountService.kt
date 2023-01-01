package com.wutsi.marketplace.access.service

import com.wutsi.marketplace.access.dao.DiscountRepository
import com.wutsi.marketplace.access.dto.CreateDiscountRequest
import com.wutsi.marketplace.access.dto.Discount
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

@Service
class DiscountService(
    private val dao: DiscountRepository,
    private val storeService: StoreService,
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
}
