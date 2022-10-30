package com.wutsi.marketplace.access.dao

import com.wutsi.marketplace.access.entity.StoreEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StoreRepository : CrudRepository<StoreEntity, Long> {
    fun findByAccountIdAndIsDeleted(accountId: Long, isDeleted: Boolean): List<StoreEntity>
    fun findByIdInAndIsDeleted(id: List<Long>, isDeleted: Boolean, pagination: Pageable): List<StoreEntity>
    fun findByIsDeleted(isDeleted: Boolean, pagination: Pageable): List<StoreEntity>
}
