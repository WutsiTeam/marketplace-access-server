package com.wutsi.marketplace.access.dto

import kotlin.Int
import kotlin.Long

public data class Store(
    public val id: Long? = null,
    public val totalProductCount: Int = 0,
    public val totalPublishedProductCount: Int = 0
)
