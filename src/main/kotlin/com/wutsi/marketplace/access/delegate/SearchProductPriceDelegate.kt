package com.wutsi.marketplace.access.delegate

import com.wutsi.marketplace.access.dto.SearchProductPriceRequest
import com.wutsi.marketplace.access.dto.SearchProductPriceResponse
import com.wutsi.marketplace.access.service.PriceService
import com.wutsi.platform.core.logging.KVLogger
import org.springframework.stereotype.Service

@Service
public class SearchProductPriceDelegate(
    private val logger: KVLogger,
    private val service: PriceService,
) {
    public fun invoke(request: SearchProductPriceRequest): SearchProductPriceResponse {
        logger.add("request_product_ids", request.productIds)

        val prices = service.searchPrices(request)
        logger.add("response_count", prices.size)

        return SearchProductPriceResponse(prices = prices)
    }
}
