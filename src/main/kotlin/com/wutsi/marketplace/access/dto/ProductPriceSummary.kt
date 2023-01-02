package com.wutsi.marketplace.access.dto

import kotlin.Int
import kotlin.Long

public data class ProductPriceSummary(
    public val productId: Long = 0,
    public val price: Long = 0,
    public val referencePrice: Long? = null,
    public val discountId: Long? = null,
    public val savings: Long = 0,
    public val savingsPercentage: Int = 0,
)
