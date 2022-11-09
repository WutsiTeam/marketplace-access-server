package com.wutsi.marketplace.access.endpoint

import com.wutsi.marketplace.access.`delegate`.CancelReservationDelegate
import org.springframework.web.bind.`annotation`.DeleteMapping
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.RestController
import kotlin.Long

@RestController
public class CancelReservationController(
    public val `delegate`: CancelReservationDelegate
) {
    @DeleteMapping("/v1/reservations/{id}")
    public fun invoke(@PathVariable(name = "id") id: Long) {
        delegate.invoke(id)
    }
}
