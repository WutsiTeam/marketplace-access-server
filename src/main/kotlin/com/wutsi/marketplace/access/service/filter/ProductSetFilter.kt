package com.wutsi.marketplace.access.service.filter

import com.wutsi.marketplace.access.entity.ProductEntity

class ProductSetFilter(
    private val filters: List<ProductFilter>,
) : ProductFilter {
    override fun filter(products: List<ProductEntity>): List<ProductEntity> {
        var result = products
        filters.forEach {
            result = it.filter(result)
        }
        return result
    }
}
