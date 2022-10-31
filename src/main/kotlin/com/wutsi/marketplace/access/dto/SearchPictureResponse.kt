package com.wutsi.marketplace.access.dto

public data class SearchPictureResponse(
    public val pictures: List<PictureSummary> = emptyList()
)
