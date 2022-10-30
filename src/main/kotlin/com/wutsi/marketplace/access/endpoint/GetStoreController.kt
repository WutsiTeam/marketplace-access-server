package com.wutsi.marketplace.access.endpoint

import com.wutsi.marketplace.access.`delegate`.GetStoreDelegate
import com.wutsi.marketplace.access.dto.GetStoreResponse
import org.springframework.web.bind.`annotation`.GetMapping
import org.springframework.web.bind.`annotation`.RestController

@RestController
public class GetStoreController(
    public val `delegate`: GetStoreDelegate
) {
    @GetMapping("/v1/stores/{id}")
    public fun invoke(): GetStoreResponse = delegate.invoke()
}
