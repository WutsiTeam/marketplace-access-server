package com.wutsi.marketplace.access.dao

import com.wutsi.enums.StoreStatus
import com.wutsi.marketplace.access.entity.StoreEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StoreRepository : CrudRepository<StoreEntity, Long> {
    fun findByAccountIdAndStatusNot(accountId: Long, status: StoreStatus): List<StoreEntity>
}
