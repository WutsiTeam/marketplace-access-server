package com.wutsi.marketplace.access.`delegate`

import com.wutsi.marketplace.access.service.StoreService
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class DeleteStoreDelegate(private val service: StoreService) {
    @Transactional
    fun invoke(id: Long) {
        service.delete(id)
    }
}
