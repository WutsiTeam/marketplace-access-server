package com.wutsi.marketplace.access.`delegate`

import com.wutsi.marketplace.access.dto.CheckProductAvailabilityRequest
import com.wutsi.marketplace.access.service.ProductService
import org.springframework.stereotype.Service

@Service
class CheckProductAvailabilityDelegate(private val service: ProductService) {
    fun invoke(request: CheckProductAvailabilityRequest) {
        service.checkAvailability(request)
    }
}
