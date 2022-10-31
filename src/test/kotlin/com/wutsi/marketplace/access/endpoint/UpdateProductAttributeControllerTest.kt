package com.wutsi.marketplace.access.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.marketplace.access.dao.ProductRepository
import com.wutsi.marketplace.access.dto.UpdateProductAttributeRequest
import com.wutsi.marketplace.access.error.ErrorURN
import com.wutsi.platform.core.error.ErrorResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import kotlin.test.assertEquals
import kotlin.test.assertNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/UpdateProductAttributeController.sql"])
class UpdateProductAttributeControllerTest {
    companion object {
        const val PRODUCT_ID = 100L
    }

    @LocalServerPort
    val port: Int = 0

    private val rest = RestTemplate()

    @Autowired
    private lateinit var dao: ProductRepository

    @Test
    fun title() {
        val request = UpdateProductAttributeRequest("THIS IS THE VALUE")
        val response = rest.postForEntity(url("title"), request, Any::class.java)

        assertEquals(200, response.statusCodeValue)

        val product = dao.findById(PRODUCT_ID).get()
        assertEquals(request.value, product.title)
    }

    @Test
    fun summary() {
        val request = UpdateProductAttributeRequest("THIS IS THE VALUE")
        val response = rest.postForEntity(url("summary"), request, Any::class.java)

        assertEquals(200, response.statusCodeValue)

        val product = dao.findById(PRODUCT_ID).get()
        assertEquals(request.value, product.summary)
    }

    @Test
    fun summaryEmpty() {
        val request = UpdateProductAttributeRequest("")
        val response = rest.postForEntity(url("summary"), request, Any::class.java)

        assertEquals(200, response.statusCodeValue)

        val product = dao.findById(PRODUCT_ID).get()
        assertNull(product.summary)
    }

    @Test
    fun description() {
        val request = UpdateProductAttributeRequest("THIS IS THE VALUE")
        val response = rest.postForEntity(url("description"), request, Any::class.java)

        assertEquals(200, response.statusCodeValue)

        val product = dao.findById(PRODUCT_ID).get()
        assertEquals(request.value, product.description)
    }

    @Test
    fun price() {
        val request = UpdateProductAttributeRequest("10000")
        val response = rest.postForEntity(url("price"), request, Any::class.java)

        assertEquals(200, response.statusCodeValue)

        val product = dao.findById(PRODUCT_ID).get()
        assertEquals(request.value?.toLong(), product.price)
    }

    @Test
    fun priceNull() {
        val request = UpdateProductAttributeRequest(null)
        val response = rest.postForEntity(url("price"), request, Any::class.java)

        assertEquals(200, response.statusCodeValue)

        val product = dao.findById(PRODUCT_ID).get()
        assertNull(product.price)
    }

    @Test
    fun priceEmpty() {
        val request = UpdateProductAttributeRequest("")
        val response = rest.postForEntity(url("price"), request, Any::class.java)

        assertEquals(200, response.statusCodeValue)

        val product = dao.findById(PRODUCT_ID).get()
        assertNull(product.price)
    }

    @Test
    fun comparablePrice() {
        val request = UpdateProductAttributeRequest("55555")
        val response = rest.postForEntity(url("comparable-price"), request, Any::class.java)

        assertEquals(200, response.statusCodeValue)

        val product = dao.findById(PRODUCT_ID).get()
        assertEquals(request.value?.toLong(), product.comparablePrice)
    }

    @Test
    fun thumbnailId() {
        val request = UpdateProductAttributeRequest("102")
        val response = rest.postForEntity(url("thumbnail-id"), request, Any::class.java)

        assertEquals(200, response.statusCodeValue)

        val product = dao.findById(PRODUCT_ID).get()
        assertEquals(request.value?.toLong(), product.thumbnail?.id)
    }

    @Test
    fun thumbnailIdInvalid() {
        val request = UpdateProductAttributeRequest("99999")
        val ex = assertThrows<HttpStatusCodeException> {
            rest.postForEntity(url("thumbnail-id"), request, Any::class.java)
        }

        assertEquals(404, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.PICTURE_NOT_FOUND.urn, response.error.code)
    }

    @Test
    fun categoryId() {
        val request = UpdateProductAttributeRequest("1100")
        val response = rest.postForEntity(url("category-id"), request, Any::class.java)

        assertEquals(200, response.statusCodeValue)

        val product = dao.findById(PRODUCT_ID).get()
        assertEquals(request.value?.toLong(), product.category?.id)
    }

    @Test
    fun SubCategoryIdInvalid() {
        val request = UpdateProductAttributeRequest("99999")
        val ex = assertThrows<HttpStatusCodeException> {
            rest.postForEntity(url("category-id"), request, Any::class.java)
        }

        assertEquals(404, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.CATEGORY_NOT_FOUND.urn, response.error.code)
    }

    @Test
    fun notFound() {
        val request = UpdateProductAttributeRequest("15000")
        val ex = assertThrows<HttpStatusCodeException> {
            rest.postForEntity(url("price", 99999), request, Any::class.java)
        }

        assertEquals(404, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.PRODUCT_NOT_FOUND.urn, response.error.code)
    }

    @Test
    fun deleted() {
        val request = UpdateProductAttributeRequest("15000")
        val ex = assertThrows<HttpStatusCodeException> {
            rest.postForEntity(url("price", 900), request, Any::class.java)
        }

        assertEquals(404, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.PRODUCT_NOT_FOUND.urn, response.error.code)
    }

    @Test
    fun badAttribute() {
        val request = UpdateProductAttributeRequest("15000")
        val ex = assertThrows<HttpStatusCodeException> {
            rest.postForEntity(url("xxxxx", 100), request, Any::class.java)
        }

        assertEquals(400, ex.rawStatusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.ATTRIBUTE_NOT_VALID.urn, response.error.code)
    }

    private fun url(name: String, productId: Long = 100L) =
        "http://localhost:$port/v1/products/$productId/attributes/$name"
}
