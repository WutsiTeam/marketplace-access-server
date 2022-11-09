package com.wutsi.marketplace.access.dto

public data class CheckProductAvailabilityRequest(
    public val items: List<ReservationItem> = emptyList()
)
