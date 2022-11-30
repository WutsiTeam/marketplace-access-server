package com.wutsi.marketplace.access.dto

public data class SearchReservationRequest(
    public val orderId: String? = null,
    public val limit: Int = 20,
    public val offset: Int = 0
)
