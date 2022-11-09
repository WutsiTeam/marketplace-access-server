package com.wutsi.marketplace.access.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.marketplace.access.dto.CheckProductAvailabilityRequest
import com.wutsi.marketplace.access.dto.ReservationProduct
import com.wutsi.marketplace.access.error.ErrorURN
import com.wutsi.platform.core.error.ErrorResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/CheckProductAvailabilityController.sql"])
class CheckProductAvailabilityControllerTest {
    @LocalServerPort
    val port: Int = 0

    private val rest = RestTemplate()

    @Test
    fun available() {
        val request = CheckProductAvailabilityRequest(
            products = listOf(
                ReservationProduct(productId = 100, quantity = 1),
                ReservationProduct(productId = 101, quantity = 100),
                ReservationProduct(productId = 102, quantity = 1),
                ReservationProduct(productId = 103, quantity = 5)
            )
        )
        val response = rest.postForEntity(url(), request, Any::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun notAvailable() {
        val request = CheckProductAvailabilityRequest(
            products = listOf(
                ReservationProduct(productId = 100, quantity = 1),
                ReservationProduct(productId = 101, quantity = 100),
                ReservationProduct(productId = 102, quantity = 100),
                ReservationProduct(productId = 103, quantity = 500)
            )
        )
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url(), request, Any::class.java)
        }

        // THEN
        assertEquals(HttpStatus.CONFLICT, ex.statusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.PRODUCT_NOT_AVAILABLE.urn, response.error.code)
        assertNotNull(response.error.data)
        assertEquals(2, response.error.data?.size)
        assertEquals(1, response.error.data?.get("102"))
        assertEquals(10, response.error.data?.get("103"))
    }

    private fun url() = "http://localhost:$port/v1/products/availability"
}
