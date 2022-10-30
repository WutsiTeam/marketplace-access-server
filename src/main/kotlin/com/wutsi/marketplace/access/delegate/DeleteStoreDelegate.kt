package com.wutsi.marketplace.access.`delegate`

import com.wutsi.marketplace.access.service.StoreService
import org.springframework.stereotype.Service

@Service
class DeleteStoreDelegate(private val service: StoreService) {
    fun invoke(id: Long) {
        service.delete(id)
    }
}
