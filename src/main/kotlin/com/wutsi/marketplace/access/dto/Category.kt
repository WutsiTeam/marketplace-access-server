package com.wutsi.marketplace.access.dto

import kotlin.Long
import kotlin.String

public data class Category(
    public val id: Long = 0,
    public val title: String = "",
    public val parentId: Long? = null
)
