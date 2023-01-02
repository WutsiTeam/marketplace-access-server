package com.wutsi.marketplace.access.dto

public data class SearchProductPriceRequest(
    public val storeId: Long = 0,
    public val productIds: List<Long> = emptyList(),
)
