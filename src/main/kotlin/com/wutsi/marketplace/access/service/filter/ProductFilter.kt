package com.wutsi.marketplace.access.service.filter

import com.wutsi.marketplace.access.entity.ProductEntity

interface ProductFilter {
    fun filter(products: List<ProductEntity>): List<ProductEntity>
}
