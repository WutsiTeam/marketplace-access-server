package com.wutsi.marketplace.access.`delegate`

import com.wutsi.marketplace.access.dto.UpdateReservationStatusRequest
import com.wutsi.marketplace.access.service.ReservationService
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UpdateReservationStatusDelegate(private val service: ReservationService) {
    @Transactional
    fun invoke(id: Long, request: UpdateReservationStatusRequest) {
        service.updateStatus(id, request)
    }
}
