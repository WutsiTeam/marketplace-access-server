package com.wutsi.marketplace.access.service

import com.wutsi.marketplace.access.dao.ProductRepository
import com.wutsi.marketplace.access.dao.StoreRepository
import com.wutsi.marketplace.access.dto.CreateStoreRequest
import com.wutsi.marketplace.access.dto.SearchStoreRequest
import com.wutsi.marketplace.access.dto.Store
import com.wutsi.marketplace.access.dto.StoreSummary
import com.wutsi.marketplace.access.dto.UpdateStoreStatusRequest
import com.wutsi.marketplace.access.entity.StoreEntity
import com.wutsi.marketplace.access.enums.ProductStatus
import com.wutsi.marketplace.access.enums.StoreStatus
import com.wutsi.marketplace.access.error.ErrorURN
import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType
import com.wutsi.platform.core.error.exception.BadRequestException
import com.wutsi.platform.core.error.exception.NotFoundException
import org.springframework.stereotype.Service
import java.time.ZoneOffset
import java.util.Date
import javax.persistence.EntityManager
import javax.persistence.Query

@Service
class StoreService(
    private val dao: StoreRepository,
    private val productDao: ProductRepository,
    private val em: EntityManager
) {
    fun findById(id: Long): StoreEntity =
        dao.findById(id)
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

    fun create(request: CreateStoreRequest): StoreEntity {
        val stores = dao.findByAccountIdAndStatusNot(request.accountId, StoreStatus.SUSPENDED)
        return if (stores.isEmpty()) {
            dao.save(
                StoreEntity(
                    accountId = request.accountId,
                    currency = request.currency,
                    status = StoreStatus.ACTIVE
                )
            )
        } else {
            stores[0]
        }
    }

    fun updateStatus(id: Long, request: UpdateStoreStatusRequest) {
        val store = findById(id)
        val status = StoreStatus.valueOf(request.status)
        if (store.status == status) {
            return
        }

        store.status = status
        when (status) {
            StoreStatus.SUSPENDED -> store.suspended = Date()
            StoreStatus.UNDER_REVIEW -> store.suspended = null
            StoreStatus.ACTIVE -> store.suspended = null
            else -> throw BadRequestException(
                error = Error(
                    code = ErrorURN.STATUS_NOT_VALID.urn,
                    parameter = Parameter(
                        name = "status",
                        value = request.status,
                        type = ParameterType.PARAMETER_TYPE_PAYLOAD
                    )
                )
            )
        }
        dao.save(store)
    }

    fun search(request: SearchStoreRequest): List<StoreEntity> {
        val sql = sql(request)
        val query = em.createQuery(sql)
        parameters(request, query)
        return query
            .setFirstResult(request.offset)
            .setMaxResults(request.limit)
            .resultList as List<StoreEntity>
    }

    private fun sql(request: SearchStoreRequest): String {
        val select = select()
        val where = where(request)
        return if (where.isNullOrEmpty()) {
            select
        } else {
            "$select WHERE $where"
        }
    }

    private fun select(): String =
        "SELECT P FROM StoreEntity P"

    private fun where(request: SearchStoreRequest): String {
        val criteria = mutableListOf<String>()

        if (!request.status.isNullOrEmpty()) {
            criteria.add("P.status = :status")
        }
        if (request.storeIds.isNotEmpty()) {
            criteria.add("P.id IN :store_ids")
        }

        return criteria.joinToString(separator = " AND ")
    }

    private fun parameters(request: SearchStoreRequest, query: Query) {
        if (!request.status.isNullOrEmpty()) {
            query.setParameter("status", StoreStatus.valueOf(request.status.uppercase()))
        }
        if (request.storeIds.isNotEmpty()) {
            query.setParameter("store_ids", request.storeIds)
        }
    }

    fun toStore(store: StoreEntity) = Store(
        id = store.id ?: -1,
        accountId = store.accountId,
        productCount = store.productCount,
        publishedProductCount = store.publishedProductCount,
        created = store.created.toInstant().atOffset(ZoneOffset.UTC),
        updated = store.updated.toInstant().atOffset(ZoneOffset.UTC),
        suspended = store.suspended?.toInstant()?.atOffset(ZoneOffset.UTC),
        currency = store.currency,
        status = store.status.name
    )

    fun toStoreSummary(store: StoreEntity) = StoreSummary(
        id = store.id ?: -1,
        accountId = store.accountId,
        productCount = store.productCount,
        publishedProductCount = store.publishedProductCount,
        created = store.created.toInstant().atOffset(ZoneOffset.UTC),
        updated = store.updated.toInstant().atOffset(ZoneOffset.UTC),
        suspended = store.suspended?.toInstant()?.atOffset(ZoneOffset.UTC),
        status = store.status.name
    )

    fun updateProductCount(store: StoreEntity) {
        store.productCount = productDao.countByStoreAndIsDeleted(store, false)
        store.publishedProductCount =
            productDao.countByStoreAndIsDeletedAndStatus(store, false, ProductStatus.PUBLISHED)
        dao.save(store)
    }
}
