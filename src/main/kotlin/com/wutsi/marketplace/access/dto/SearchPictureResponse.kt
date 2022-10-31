package com.wutsi.marketplace.access.dto

import kotlin.collections.List

public data class SearchPictureResponse(
    public val pictureId: List<PictureSummary> = emptyList()
)
