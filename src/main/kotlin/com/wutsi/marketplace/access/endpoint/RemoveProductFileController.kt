package com.wutsi.marketplace.access.endpoint

import com.wutsi.marketplace.access.`delegate`.RemoveProductFileDelegate
import org.springframework.web.bind.`annotation`.DeleteMapping
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.RestController
import kotlin.Long

@RestController
public class RemoveProductFileController(
    public val `delegate`: RemoveProductFileDelegate
) {
    @DeleteMapping("/v1/products/files/{id}")
    public fun invoke(@PathVariable(name = "id") id: Long) {
        delegate.invoke(id)
    }
}
