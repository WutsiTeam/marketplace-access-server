package com.wutsi.marketplace.access.endpoint

import com.wutsi.marketplace.access.`delegate`.DeleteStoreDelegate
import org.springframework.web.bind.`annotation`.DeleteMapping
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.RestController
import kotlin.Long

@RestController
public class DeleteStoreController(
    public val `delegate`: DeleteStoreDelegate
) {
    @DeleteMapping("/v1/stores/{id}")
    public fun invoke(@PathVariable(name = "id") id: Long) {
        delegate.invoke(id)
    }
}
