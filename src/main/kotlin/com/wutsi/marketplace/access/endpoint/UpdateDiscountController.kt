package com.wutsi.marketplace.access.endpoint

import com.wutsi.marketplace.access.`delegate`.UpdateDiscountDelegate
import com.wutsi.marketplace.access.dto.UpdateDiscountRequest
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid
import kotlin.Long

@RestController
public class UpdateDiscountController(
    public val `delegate`: UpdateDiscountDelegate,
) {
    @PostMapping("/v1/discounts/{id}")
    public fun invoke(
        @PathVariable(name = "id") id: Long,
        @Valid @RequestBody
        request: UpdateDiscountRequest,
    ) {
        delegate.invoke(id, request)
    }
}
