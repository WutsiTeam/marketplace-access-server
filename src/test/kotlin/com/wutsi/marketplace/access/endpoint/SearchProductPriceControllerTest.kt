package com.wutsi.marketplace.access.endpoint

import com.wutsi.marketplace.access.dto.SearchProductPriceRequest
import com.wutsi.marketplace.access.dto.SearchProductPriceResponse
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.RestTemplate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/SearchProductPriceController.sql"])
class SearchProductPriceControllerTest {
    @LocalServerPort
    val port: Int = 0

    private val rest = RestTemplate()

    @Test
    fun `product without discount`() {
        // WHEN
        val request = SearchProductPriceRequest(
            storeId = 3L,
            productIds = listOf(300L),
        )
        val response = rest.postForEntity(url(), request, SearchProductPriceResponse::class.java)

        // THEN
        assertEquals(HttpStatus.OK, response.statusCode)

        val prices = response.body!!.prices
        assertEquals(1, prices.size)
        assertEquals(300L, prices[0].productId)
        assertEquals(300000L, prices[0].price)
        assertNull(prices[0].referencePrice)
        assertNull(prices[0].discountId)
        assertEquals(0L, prices[0].savings)
        assertEquals(0, prices[0].savingsPercentage)
        assertNull(prices[0].expires)
    }

    @Test
    fun `product with discounts applied to all products`() {
        // WHEN
        val request = SearchProductPriceRequest(
            storeId = 1L,
            productIds = listOf(100L),
        )
        val response = rest.postForEntity(url(), request, SearchProductPriceResponse::class.java)

        // THEN
        assertEquals(HttpStatus.OK, response.statusCode)

        val prices = response.body!!.prices
        assertEquals(1, prices.size)
        assertEquals(100L, prices[0].productId)
        assertEquals(112500L, prices[0].price)
        assertEquals(150000L, prices[0].referencePrice)
        assertEquals(101L, prices[0].discountId)
        assertEquals(37500L, prices[0].savings)
        assertEquals(25, prices[0].savingsPercentage)
        assertNotNull(prices[0].expires)
    }

    @Test
    fun `product with discounts applied to specific products`() {
        // WHEN
        val request = SearchProductPriceRequest(
            storeId = 2L,
            productIds = listOf(200L),
        )
        val response = rest.postForEntity(url(), request, SearchProductPriceResponse::class.java)

        // THEN
        assertEquals(HttpStatus.OK, response.statusCode)

        val prices = response.body!!.prices
        assertEquals(1, prices.size)
        assertEquals(200L, prices[0].productId)
        assertEquals(1600L, prices[0].price)
        assertEquals(2000L, prices[0].referencePrice)
        assertEquals(200L, prices[0].discountId)
        assertEquals(400L, prices[0].savings)
        assertEquals(20, prices[0].savingsPercentage)
        assertNotNull(prices[0].expires)
    }

    @Test
    fun `search multiple products`() {
        // WHEN
        val request = SearchProductPriceRequest(
            storeId = 5L,
        )
        val response = rest.postForEntity(url(), request, SearchProductPriceResponse::class.java)

        // THEN
        assertEquals(HttpStatus.OK, response.statusCode)

        val prices = response.body!!.prices
        assertEquals(3, prices.size)

        assertEquals(500L, prices[0].productId)
        assertEquals(1600L, prices[0].price)
        assertEquals(2000L, prices[0].referencePrice)
        assertEquals(500L, prices[0].discountId)
        assertEquals(400L, prices[0].savings)
        assertEquals(20, prices[0].savingsPercentage)
        assertNotNull(prices[0].expires)

        assertEquals(501L, prices[1].productId)
        assertEquals(1500L, prices[1].price)
        assertEquals(2000L, prices[1].referencePrice)
        assertEquals(501L, prices[1].discountId)
        assertEquals(500L, prices[1].savings)
        assertEquals(25, prices[1].savingsPercentage)
        assertNotNull(prices[1].expires)

        assertEquals(502L, prices[2].productId)
        assertEquals(240000L, prices[2].price)
        assertEquals(300000L, prices[2].referencePrice)
        assertEquals(500L, prices[2].discountId)
        assertEquals(60000L, prices[2].savings)
        assertEquals(20, prices[2].savingsPercentage)
        assertNotNull(prices[2].expires)
    }

    private fun url() = "http://localhost:$port/v1/products/prices"
}
