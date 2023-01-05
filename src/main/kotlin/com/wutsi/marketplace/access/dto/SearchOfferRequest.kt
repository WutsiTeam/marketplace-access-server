package com.wutsi.marketplace.access.dto

import kotlin.Int
import kotlin.Long
import kotlin.String

public data class SearchOfferRequest(
    public val storeId: Long = 0,
    public val limit: Int = 100,
    public val offset: Int = 0,
    public val sortBy: String? = null,
)
