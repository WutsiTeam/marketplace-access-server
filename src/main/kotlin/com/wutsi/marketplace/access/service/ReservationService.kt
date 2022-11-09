package com.wutsi.marketplace.access.service

import com.wutsi.marketplace.access.dao.ReservationItemRepository
import com.wutsi.marketplace.access.dao.ReservationRepository
import com.wutsi.marketplace.access.dto.CreateReservationRequest
import com.wutsi.marketplace.access.dto.SearchProductRequest
import com.wutsi.marketplace.access.entity.ReservationEntity
import com.wutsi.marketplace.access.entity.ReservationItemEntity
import com.wutsi.marketplace.access.enums.ReservationStatus
import com.wutsi.marketplace.access.error.ErrorURN
import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.exception.ConflictException
import org.springframework.stereotype.Service

@Service
class ReservationService(
    private val dao: ReservationRepository,
    private val itemDao: ReservationItemRepository,
    private val productService: ProductService
) {
    fun create(request: CreateReservationRequest): ReservationEntity {
        // Reservation
        val reservation = dao.save(
            ReservationEntity(
                orderId = request.orderId,
                status = ReservationStatus.PENDING
            )
        )

        // Items
        val productMap = productService.search(
            request = SearchProductRequest(
                productIds = request.items.map { it.productId },
                limit = request.items.size
            )
        ).associateBy { it.id }
        reservation.items = request.items.map {
            ReservationItemEntity(
                reservation = reservation,
                quantity = it.quantity,
                product = productMap[it.productId]
                    ?: throw ConflictException(
                        error = Error(
                            code = ErrorURN.PRODUCT_NOT_AVAILABLE.urn,
                            data = mapOf(
                                "product-id" to it.productId
                            )
                        )
                    )
            )
        }
        itemDao.saveAll(reservation.items)

        return reservation
    }
}
