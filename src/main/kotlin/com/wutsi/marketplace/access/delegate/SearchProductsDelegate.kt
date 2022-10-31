package com.wutsi.marketplace.access.`delegate`

import com.wutsi.marketplace.access.dto.SearchProductRequest
import com.wutsi.marketplace.access.dto.SearchProductResponse
import org.springframework.stereotype.Service

@Service
public class SearchProductsDelegate() {
    public fun invoke(request: SearchProductRequest): SearchProductResponse {
        TODO()
    }
}
