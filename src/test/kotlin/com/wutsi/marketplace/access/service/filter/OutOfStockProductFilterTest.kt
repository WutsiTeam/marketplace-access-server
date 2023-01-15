package com.wutsi.marketplace.access.service.filter

import com.wutsi.marketplace.access.entity.ProductEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class OutOfStockProductFilterTest {
    private val filter = OutOfStockProductFilter()

    @Test
    fun filter() {
        val p1 = ProductEntity(id = 1, quantity = 0)
        val p2 = ProductEntity(id = 2, quantity = null)
        val p3 = ProductEntity(id = 3, quantity = 10)

        val result = filter.filter(listOf(p1, p2, p3))

        assertEquals(listOf(p2, p3, p1), result)
    }
}
