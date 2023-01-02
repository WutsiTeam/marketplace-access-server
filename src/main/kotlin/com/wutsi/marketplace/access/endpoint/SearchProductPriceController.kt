package com.wutsi.marketplace.access.endpoint

import com.wutsi.marketplace.access.`delegate`.SearchProductPriceDelegate
import com.wutsi.marketplace.access.dto.SearchProductPriceRequest
import com.wutsi.marketplace.access.dto.SearchProductPriceResponse
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid

@RestController
public class SearchProductPriceController(
    public val `delegate`: SearchProductPriceDelegate,
) {
    @PostMapping("/v1/products/prices")
    public fun invoke(
        @Valid @RequestBody
        request: SearchProductPriceRequest,
    ):
        SearchProductPriceResponse = delegate.invoke(request)
}
