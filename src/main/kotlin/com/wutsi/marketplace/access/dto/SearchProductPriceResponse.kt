package com.wutsi.marketplace.access.dto

import kotlin.collections.List

public data class SearchProductPriceResponse(
    public val prices: List<ProductPriceSummary> = emptyList(),
)
