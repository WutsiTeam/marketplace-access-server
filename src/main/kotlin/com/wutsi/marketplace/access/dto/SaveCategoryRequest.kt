package com.wutsi.marketplace.access.dto

import javax.validation.constraints.NotBlank
import kotlin.Long
import kotlin.String

public data class SaveCategoryRequest(
    public val parentId: Long? = null,
    @get:NotBlank
    public val title: String = "",
)
