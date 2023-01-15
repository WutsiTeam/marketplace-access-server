package com.wutsi.marketplace.access.service.filter

import com.wutsi.marketplace.access.entity.ProductEntity

/**
 * Bubble down out of stock products
 */
class OutOfStockProductFilter : ProductFilter {
    override fun filter(products: List<ProductEntity>): List<ProductEntity> {
        val result = mutableListOf<ProductEntity>()
        result.addAll(products.filter { !it.outOfScope })
        result.addAll(products.filter { it.outOfScope })
        return result
    }
}
