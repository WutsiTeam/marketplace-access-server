package com.wutsi.marketplace.access.dto

import kotlin.Int
import kotlin.Long
import kotlin.collections.List

public data class SearchStoreRequest(
    public val storeIds: List<Long> = emptyList(),
    public val limit: Int = 100,
    public val offset: Int = 0
)
