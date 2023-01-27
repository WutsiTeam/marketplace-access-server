package com.wutsi.marketplace.access.endpoint

import com.wutsi.marketplace.access.`delegate`.UpdateStorePolicyDelegate
import com.wutsi.marketplace.access.dto.UpdateStorePolicyAttributeRequest
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid
import kotlin.Long

@RestController
public class UpdateStorePolicyController(
    public val `delegate`: UpdateStorePolicyDelegate,
) {
    @PostMapping("/v1/stores/{id}/policies")
    public fun invoke(
        @PathVariable(name = "id") id: Long,
        @Valid @RequestBody
        request: UpdateStorePolicyAttributeRequest,
    ) {
        delegate.invoke(id, request)
    }
}
