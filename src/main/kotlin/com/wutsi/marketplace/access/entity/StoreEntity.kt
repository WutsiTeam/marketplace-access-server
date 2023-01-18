package com.wutsi.marketplace.access.entity

import com.wutsi.enums.StoreStatus
import java.util.Date
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "T_STORE")
data class StoreEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val accountId: Long = -1,
    val businessId: Long = -1,
    var productCount: Int = 0,
    var publishedProductCount: Int = 0,
    val currency: String = "",
    var status: StoreStatus = StoreStatus.UNKNOWN,

    val created: Date = Date(),
    val updated: Date = Date(),
    var deactivated: Date? = null,
)
