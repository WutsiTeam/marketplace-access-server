package com.wutsi.marketplace.access.service

import com.wutsi.marketplace.access.dao.StoreRepository
import com.wutsi.marketplace.access.dto.CreateStoreRequest
import com.wutsi.marketplace.access.dto.Store
import com.wutsi.marketplace.access.entity.StoreEntity
import com.wutsi.marketplace.access.error.ErrorURN
import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType
import com.wutsi.platform.core.error.exception.NotFoundException
import org.springframework.stereotype.Service
import java.time.ZoneOffset
import java.util.Date

@Service
class StoreService(
    private val dao: StoreRepository
) {
    fun findById(id: Long): StoreEntity {
        val store = dao.findById(id)
            .orElseThrow {
                NotFoundException(
                    error = Error(
                        code = ErrorURN.STORE_NOT_FOUND.urn,
                        parameter = Parameter(
                            name = "id",
                            value = id,
                            type = ParameterType.PARAMETER_TYPE_PATH
                        )
                    )
                )
            }

        if (store.isDeleted) {
            throw NotFoundException(
                error = Error(
                    code = ErrorURN.STORE_DELETED.urn,
                    parameter = Parameter(
                        name = "id",
                        value = id,
                        type = ParameterType.PARAMETER_TYPE_PATH
                    )
                )
            )
        }
        return store
    }

    fun delete(id: Long) {
        val store = findById(id)
        store.isDeleted = true
        store.deleted = Date()
        dao.save(store)
    }

    fun create(request: CreateStoreRequest): StoreEntity {
        val stores = dao.findByAccountIdAndIsDeleted(request.accountId, false)
        return if (stores.isEmpty()) {
            dao.save(
                StoreEntity(
                    accountId = request.accountId
                )
            )
        } else {
            stores[0]
        }
    }

    fun toStore(store: StoreEntity) = Store(
        id = store.id ?: -1,
        accountId = store.accountId,
        productCount = store.productCount,
        publishedProductCount = store.publishedProductCount,
        created = store.created.toInstant().atOffset(ZoneOffset.UTC),
        updated = store.updated.toInstant().atOffset(ZoneOffset.UTC)
    )
}
