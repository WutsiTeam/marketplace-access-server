package com.wutsi.marketplace.access.dto

import org.springframework.format.annotation.DateTimeFormat
import java.time.OffsetDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

public data class UpdateProductEventRequest(
    @get:NotBlank
    public val provider: String = "",
    @get:NotBlank
    public val meetingId: String = "",
    public val meetingPassword: String? = null,
    @get:NotNull
    @get:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public val starts: OffsetDateTime = OffsetDateTime.now(),
    @get:NotNull
    @get:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public val ends: OffsetDateTime = OffsetDateTime.now()
)
