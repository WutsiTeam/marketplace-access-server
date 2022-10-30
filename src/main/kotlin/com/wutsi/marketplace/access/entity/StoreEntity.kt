package com.wutsi.marketplace.access.entity

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "T_STORE")
data class StoreEntity(
    @Id
    val id: Long = -1,

    var productCount: Int = 0,
    var publishedProductCount: Int = 0
)
