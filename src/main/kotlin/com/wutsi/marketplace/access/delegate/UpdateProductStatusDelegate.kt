package com.wutsi.marketplace.access.`delegate`

import com.wutsi.marketplace.access.dto.UpdateProductStatusRequest
import org.springframework.stereotype.Service
import kotlin.Long

@Service
public class UpdateProductStatusDelegate() {
    public fun invoke(id: Long, request: UpdateProductStatusRequest) {
        TODO()
    }
}
