package com.wutsi.marketplace.access.dto

import kotlin.Int
import kotlin.Long

public data class SearchPictureRequest(
    public val productId: Long? = null,
    public val storeId: Long? = null,
    public val limit: Int = 100,
    public val offset: Int = 0
)
