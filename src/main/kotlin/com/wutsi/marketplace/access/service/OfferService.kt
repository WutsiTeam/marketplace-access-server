package com.wutsi.marketplace.access.service

import com.wutsi.enums.ProductStatus
import com.wutsi.marketplace.access.dto.Offer
import com.wutsi.marketplace.access.dto.OfferSummary
import com.wutsi.marketplace.access.dto.SearchOfferRequest
import com.wutsi.marketplace.access.dto.SearchProductRequest
import com.wutsi.marketplace.access.error.ErrorURN
import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.exception.NotFoundException
import org.springframework.stereotype.Service

@Service
class OfferService(
    private val productService: ProductService,
    private val priceService: PriceService,
) {
    fun search(request: SearchOfferRequest, language: String?): List<OfferSummary> {
        val products = productService.search(
            request = SearchProductRequest(
                status = ProductStatus.PUBLISHED.name,
                storeId = request.storeId,
                sortBy = request.sortBy,
                limit = request.limit,
                offset = request.offset,
            ),
        )
        val prices = priceService.searchPrices(products).associateBy { it.productId }

        return products.mapNotNull {
            val price = prices[it.id]
            if (price == null) {
                null
            } else {
                OfferSummary(
                    product = productService.toProductSummary(it, language),
                    price = price,
                )
            }
        }
    }

    fun findById(id: Long, language: String?): Offer {
        val product = productService.findById(id)
        if (product.status != ProductStatus.PUBLISHED) {
            throw NotFoundException(
                error = Error(
                    code = ErrorURN.PRODUCT_NOT_PUBLISHED.urn,
                ),
            )
        }

        val prices = priceService.searchPrices(listOf(product))
        if (prices.isEmpty()) {
            throw NotFoundException(
                error = Error(
                    code = ErrorURN.PRICE_NOT_FOUND.urn,
                ),
            )
        }

        return Offer(
            product = productService.toProduct(product, language),
            price = prices[0],
        )
    }
}
