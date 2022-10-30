package com.wutsi.marketplace.access.`delegate`

import com.wutsi.marketplace.access.dto.CreateStoreRequest
import com.wutsi.marketplace.access.dto.CreateStoreResponse
import com.wutsi.marketplace.access.service.StoreService
import com.wutsi.platform.core.logging.KVLogger
import org.springframework.stereotype.Service

@Service
class CreateStoreDelegate(
    private val service: StoreService,
    private val logger: KVLogger
) {
    fun invoke(request: CreateStoreRequest): CreateStoreResponse {
        logger.add("request_account_id", request.accountId)

        val store = service.create(request)
        return CreateStoreResponse(
            storeId = store.id ?: -1
        )
    }
}
