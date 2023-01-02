package com.wutsi.marketplace.access.dto

import kotlin.Long
import kotlin.collections.List

public data class SearchProductPriceRequest(
    public val storeId: Long = 0,
    public val productIds: List<Long> = emptyList(),
)
