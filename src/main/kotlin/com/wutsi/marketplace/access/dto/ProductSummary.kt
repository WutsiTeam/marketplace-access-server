package com.wutsi.marketplace.access.dto

import org.springframework.format.`annotation`.DateTimeFormat
import java.time.OffsetDateTime
import kotlin.Long
import kotlin.String

public data class ProductSummary(
    public val id: Long = 0,
    public val storeId: Long = 0,
    public val thumbnail: PictureSummary = PictureSummary(),
    public val title: String = "",
    public val summary: String? = null,
    public val price: Long? = null,
    public val comparablePrice: Long? = null,
    public val categoryId: Long = 0,
    public val currency: String = "",
    @get:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public val created: OffsetDateTime = OffsetDateTime.now(),
    @get:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public val updated: OffsetDateTime = OffsetDateTime.now(),
    public val status: String = ""
)
