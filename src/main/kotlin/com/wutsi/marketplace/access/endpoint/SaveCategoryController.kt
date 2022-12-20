package com.wutsi.marketplace.access.endpoint

import com.wutsi.marketplace.access.`delegate`.SaveCategoryDelegate
import com.wutsi.marketplace.access.dto.SaveCategoryRequest
import org.springframework.web.bind.`annotation`.PathVariable
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid
import kotlin.Long

@RestController
public class SaveCategoryController(
    public val `delegate`: SaveCategoryDelegate,
) {
    @PostMapping("/v1/categories/{id}")
    public fun invoke(
        @PathVariable(name = "id") id: Long,
        @Valid @RequestBody
        request: SaveCategoryRequest,
    ) {
        delegate.invoke(id, request)
    }
}
