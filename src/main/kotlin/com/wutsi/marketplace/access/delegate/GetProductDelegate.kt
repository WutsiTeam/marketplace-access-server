package com.wutsi.marketplace.access.`delegate`

import com.wutsi.marketplace.access.dto.GetProductResponse
import org.springframework.stereotype.Service
import kotlin.Long

@Service
public class GetProductDelegate() {
    public fun invoke(id: Long): GetProductResponse {
        TODO()
    }
}
