package com.wutsi.marketplace.access.dao

import com.wutsi.marketplace.access.entity.StoreEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StoreRepository : CrudRepository<StoreEntity, Long> {
    fun findByAccountIdAndIsDeleted(accountId: Long, isDeleted: Boolean): List<StoreEntity>
}
