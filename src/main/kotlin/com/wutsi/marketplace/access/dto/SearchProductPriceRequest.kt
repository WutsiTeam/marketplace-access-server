package com.wutsi.marketplace.access.dto

import kotlin.Long
import kotlin.collections.List

public data class SearchProductPriceRequest(
    public val productIds: List<Long> = emptyList(),
)
