package com.wutsi.marketplace.access.dto

public data class CreateReservationRequest(
    public val orderId: String = "",
    public val items: List<ReservationItem> = emptyList()
)
