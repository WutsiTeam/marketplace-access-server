package com.wutsi.marketplace.access.`delegate`

import com.wutsi.marketplace.access.dto.UpdateProductEventRequest
import com.wutsi.marketplace.access.service.ProductService
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
public class UpdateProductEventDelegate(private val service: ProductService) {
    @Transactional
    public fun invoke(id: Long, request: UpdateProductEventRequest) {
        val product = service.findById(id)
        service.updateEvent(product, request)
    }
}
