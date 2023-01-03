package com.wutsi.marketplace.access.dto

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.OffsetDateTime

public data class DiscountSummary(
    public val id: Long = 0,
    public val storeId: Long = 0,
    public val type: String = "",
    public val name: String = "",
    public val rate: Int = 0,
    @get:DateTimeFormat(pattern = "yyyy-MM-dd")
    public val starts: LocalDate? = null,
    @get:DateTimeFormat(pattern = "yyyy-MM-dd")
    public val ends: LocalDate? = null,
    @get:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public val created: OffsetDateTime = OffsetDateTime.now(),
)
