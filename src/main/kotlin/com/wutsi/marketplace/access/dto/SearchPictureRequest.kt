package com.wutsi.marketplace.access.dto

public data class SearchPictureRequest(
    public val pictureIds: List<Long> = emptyList(),
    public val productIds: List<Long> = emptyList(),
    public val pictureUrls: List<String> = emptyList(),
    public val limit: Int = 100,
    public val offset: Int = 0
)
