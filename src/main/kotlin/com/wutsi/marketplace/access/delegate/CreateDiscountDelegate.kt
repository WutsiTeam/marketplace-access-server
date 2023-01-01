package com.wutsi.marketplace.access.delegate

import com.wutsi.marketplace.access.dto.CreateDiscountRequest
import com.wutsi.marketplace.access.dto.CreateDiscountResponse
import com.wutsi.marketplace.access.service.DiscountService
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
public class CreateDiscountDelegate(private val service: DiscountService) {
    @Transactional
    public fun invoke(request: CreateDiscountRequest): CreateDiscountResponse {
        val discount = service.create(request)
        return CreateDiscountResponse(
            discountId = discount.id ?: -1,
        )
    }
}
