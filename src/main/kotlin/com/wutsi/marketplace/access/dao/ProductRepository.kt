package com.wutsi.marketplace.access.dao

import com.wutsi.marketplace.access.entity.ProductEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : CrudRepository<ProductEntity, Long>
