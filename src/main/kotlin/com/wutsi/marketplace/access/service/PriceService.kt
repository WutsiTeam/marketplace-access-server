package com.wutsi.marketplace.access.service

import com.wutsi.marketplace.access.dto.ProductPriceSummary
import com.wutsi.marketplace.access.dto.SearchDiscountRequest
import com.wutsi.marketplace.access.dto.SearchProductPriceRequest
import com.wutsi.marketplace.access.dto.SearchProductRequest
import com.wutsi.marketplace.access.entity.DiscountEntity
import com.wutsi.marketplace.access.entity.ProductEntity
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class PriceService(
    private val productService: ProductService,
    private val discountService: DiscountService,
) {
    fun searchPrices(request: SearchProductPriceRequest): List<ProductPriceSummary> {
        // Search products
        val products = productService.search(
            request = SearchProductRequest(
                productIds = request.productIds,
                limit = request.productIds.size,
            ),
        )

        // Search discounts
        val discounts = discountService.search(
            request = SearchDiscountRequest(
                productIds = request.productIds,
                date = LocalDate.now(),
                limit = request.productIds.size,
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
        )
    }
}