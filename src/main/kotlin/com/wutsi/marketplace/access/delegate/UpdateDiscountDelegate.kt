package com.wutsi.marketplace.access.delegate

import com.wutsi.marketplace.access.dto.UpdateDiscountRequest
import com.wutsi.marketplace.access.service.DiscountService
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
public class UpdateDiscountDelegate(private val service: DiscountService) {
    @Transactional
    public fun invoke(id: Long, request: UpdateDiscountRequest) {
        service.update(id, request)
    }
}
