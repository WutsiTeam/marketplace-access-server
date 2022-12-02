package com.wutsi.marketplace.access.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.enums.ProductStatus
import com.wutsi.marketplace.access.dto.GetCategoryResponse
import com.wutsi.marketplace.access.dto.GetProductResponse
import com.wutsi.marketplace.access.error.ErrorURN
import com.wutsi.platform.core.error.ErrorResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpClientErrorException
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/GetProductController.sql"])
class GetProductControllerTest : AbstractLanguageAwareControllerTest() {
    @LocalServerPort
    val port: Int = 0

    @Test
    fun get() {
        val response = rest.getForEntity(url(100), GetProductResponse::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)

        val product = response.body!!.product
        assertEquals(1L, product.store.id)
        assertEquals(11L, product.store.accountId)
        assertEquals("XAF", product.store.currency)
        assertEquals("TV", product.title)
        assertEquals("summary of TV", product.summary)
        assertEquals("description of TV", product.description)
        assertEquals(ProductStatus.PUBLISHED.name, product.status)
        assertEquals(150000L, product.price)
        assertEquals(200000L, product.comparablePrice)
        assertEquals(10, product.quantity)
        assertEquals("XAF", product.currency)
        assertEquals(200000L, product.comparablePrice)

        assertEquals(1110L, product.category?.id)
        assertEquals("Computers", product.category?.title)
        assertEquals(1100L, product.category?.parentId)

        assertEquals(101, product.thumbnail?.id)
        assertEquals("https://www.img.com/101.png", product.thumbnail?.url)

        assertEquals(2, product.pictures.size)
        assertEquals(101, product.pictures[0].id)
        assertEquals("https://www.img.com/101.png", product.pictures[0].url)

        assertEquals(102, product.pictures[1].id)
        assertEquals("https://www.img.com/102.png", product.pictures[1].url)
    }

    @Test
    fun notFound() {
        // WHEN
        val ex = assertThrows<HttpClientErrorException> {
            rest.getForEntity(url(99999), GetCategoryResponse::class.java)
        }

        // THEN
        assertEquals(HttpStatus.NOT_FOUND, ex.statusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.PRODUCT_NOT_FOUND.urn, response.error.code)
    }

    @Test
    fun deleted() {
        // WHEN
        val ex = assertThrows<HttpClientErrorException> {
            rest.getForEntity(url(199), GetCategoryResponse::class.java)
        }

        // THEN
        assertEquals(HttpStatus.NOT_FOUND, ex.statusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.PRODUCT_DELETED.urn, response.error.code)
    }

    private fun url(id: Long) = "http://localhost:$port/v1/products/$id"
}
