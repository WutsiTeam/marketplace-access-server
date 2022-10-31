package com.wutsi.marketplace.access.dto

import org.springframework.format.`annotation`.DateTimeFormat
import java.time.OffsetDateTime
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List

public data class Product(
    public val id: Long = 0,
    public val storeId: Long = 0,
    public val thumbnail: PictureSummary = PictureSummary(),
    public val pictures: List<PictureSummary> = emptyList(),
    public val category: CategorySummary = CategorySummary(),
    public val title: String = "",
    public val summary: String? = null,
    public val description: String? = null,
    public val price: Long? = null,
    public val comparablePrice: Double? = null,
    public val currency: String = "",
    public val quantity: Int = 0,
    @get:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public val created: OffsetDateTime = OffsetDateTime.now(),
    @get:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public val updated: OffsetDateTime = OffsetDateTime.now(),
    public val status: String = "",
    @get:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public val published: OffsetDateTime? = null
)
