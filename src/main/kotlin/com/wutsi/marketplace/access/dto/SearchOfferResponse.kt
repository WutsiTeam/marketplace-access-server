package com.wutsi.marketplace.access.dto

public data class SearchOfferResponse(
    public val offers: List<OfferSummary> = emptyList(),
)
