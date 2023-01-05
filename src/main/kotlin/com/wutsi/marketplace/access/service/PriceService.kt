package com.wutsi.marketplace.access.service

import com.wutsi.enums.DiscountType
import com.wutsi.marketplace.access.dto.ProductPriceSummary
import com.wutsi.marketplace.access.dto.SearchDiscountRequest
import com.wutsi.marketplace.access.dto.SearchProductPriceRequest
import com.wutsi.marketplace.access.dto.SearchProductRequest
import com.wutsi.marketplace.access.entity.DiscountEntity
import com.wutsi.marketplace.access.entity.ProductEntity
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId

@Service
class PriceService(
    private val productService: ProductService,
    private val discountService: DiscountService,
) {
    fun searchPrices(request: SearchProductPriceRequest): List<ProductPriceSummary> {
        // Search products
        val products = productService.search(
            request = SearchProductRequest(
                storeId = request.storeId,
                productIds = request.productIds,
                limit = request.productIds.size,
            ),
        )
        return searchPrices(products)
    }

    fun searchPrices(products: List<ProductEntity>): List<ProductPriceSummary> {
        if (products.isEmpty()) {
            return emptyList()
        }

        // Search discounts
        val discounts = discountService.search(
            request = SearchDiscountRequest(
                storeId = products[0].store.id ?: -1,
                productIds = products.mapNotNull { it.id },
                date = LocalDate.now(),
                type = DiscountType.SALES.name,
                limit = 100,
            ),
        )

        // Compute price with savings
        val prices = mutableListOf<ProductPriceSummary>()
        for (discount in discounts) {
            for (product in products) {
                if (canApply(product, discount)) {
                    prices.add(computePrice(product, discount))
                }
            }
        }
        val result = prices.groupBy { it.productId }
            .map { it.value.reduce { acc, cur -> if (acc.savings > cur.savings) acc else cur } /* Select the price with highest savings */ }
            .toMutableList()

        // Add price without savings
        val productIds = result.map { it.productId }
        products.filter { !productIds.contains(it.id) }
            .forEach {
                result.add(computePrice(it))
            }

        return result
    }

    private fun canApply(product: ProductEntity, discount: DiscountEntity): Boolean =
        discount.allProducts || discount.products.contains(product)

    private fun computePrice(product: ProductEntity): ProductPriceSummary =
        ProductPriceSummary(
            productId = product.id ?: -1,
            price = product.price ?: 0,
        )

    private fun computePrice(product: ProductEntity, discount: DiscountEntity): ProductPriceSummary {
        val price = product.price ?: 0L
        if (price == 0L) {
            return ProductPriceSummary(
                productId = product.id ?: -1,
                price = price,
            )
        }

        val savings = (price * discount.rate) / 100
        return ProductPriceSummary(
            productId = product.id ?: -1,
            discountId = discount.id,
            savings = savings,
            savingsPercentage = (savings * 100L / price).toInt(),
            referencePrice = price,
            price = price - savings,
            expires = discount.ends?.let { OffsetDateTime.ofInstant(it.toInstant(), ZoneId.of("UTC")) },
        )
    }
}
