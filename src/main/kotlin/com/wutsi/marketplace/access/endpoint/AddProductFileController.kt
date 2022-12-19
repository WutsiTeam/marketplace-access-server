package com.wutsi.marketplace.access.endpoint

import com.wutsi.marketplace.access.`delegate`.AddProductFileDelegate
import com.wutsi.marketplace.access.dto.AddProductFileRequest
import com.wutsi.marketplace.access.dto.AddProductFileResponse
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid
import kotlin.Long

@RestController
public class AddProductFileController(
    public val `delegate`: AddProductFileDelegate
) {
    @PostMapping("/v1/products/{id}/files")
    public fun invoke(
        @PathVariable(name = "id") id: Long,
        @Valid @RequestBody
        request: AddProductFileRequest
    ): AddProductFileResponse = delegate.invoke(id, request)
}
