package com.wutsi.marketplace.access.dto

import javax.validation.constraints.NotBlank
import kotlin.Int
import kotlin.String

public data class AddProductFileRequest(
    @get:NotBlank
    public val url: String = "",
    @get:NotBlank
    public val contentType: String = "",
    public val contentSize: Int = 0
)
