package com.wutsi.marketplace.access.dto

import kotlin.collections.List

public data class CheckProductAvailabilityRequest(
    public val products: List<ReservationProduct> = emptyList()
)
