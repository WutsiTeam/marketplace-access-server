package com.wutsi.marketplace.access.delegate

import com.wutsi.marketplace.access.dto.UpdateDiscountRequest
import com.wutsi.marketplace.access.service.DiscountService
import com.wutsi.platform.core.logging.KVLogger
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
public class UpdateDiscountDelegate(
    private val logger: KVLogger,
    private val service: DiscountService,
) {
    @Transactional
    public fun invoke(id: Long, request: UpdateDiscountRequest) {
        logger.add("request_name", request.name)
        logger.add("request_all_products", request.allProducts)
        logger.add("request_starts", request.starts)
        logger.add("request_ends", request.ends)
        logger.add("request_rate", request.rate)

        service.update(id, request)
    }
}
