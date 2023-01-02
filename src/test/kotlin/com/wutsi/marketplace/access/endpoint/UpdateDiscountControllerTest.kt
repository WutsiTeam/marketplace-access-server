package com.wutsi.marketplace.access.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.marketplace.access.dao.DiscountRepository
import com.wutsi.marketplace.access.dto.UpdateDiscountRequest
import com.wutsi.marketplace.access.error.ErrorURN
import com.wutsi.platform.core.error.ErrorResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/UpdateDiscountController.sql"])
class UpdateDiscountControllerTest {
    @LocalServerPort
    val port: Int = 0

    @Autowired
    private lateinit var dao: DiscountRepository

    private val rest = RestTemplate()

    private val request = UpdateDiscountRequest(
        name = "FIN50",
        rate = 50,
        starts = LocalDate.now().plusDays(10),
        ends = LocalDate.now().plusDays(30),
        allProducts = false,
    )

    @Test
    fun update() {
        val response = rest.postForEntity(url(100), request, Any::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)

        val discount = dao.findById(100L).get()
        assertEquals(request.name, discount.name)
        assertEquals(request.starts.toEpochDay(), discount.starts.time / 86400000)
        assertEquals(request.ends.toEpochDay(), discount.ends.time / 86400000)
        assertEquals(request.allProducts, discount.allProducts)
        assertEquals(request.rate, discount.rate)
        assertFalse(discount.isDeleted)
        assertNull(discount.deleted)
    }

    @Test
    fun deleted() {
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url(199), request, Any::class.java)
        }

        assertEquals(HttpStatus.NOT_FOUND, ex.statusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.DISCOUNT_DELETED.urn, response.error.code)
    }

    @Test
    fun notFound() {
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url(9999), request, Any::class.java)
        }

        assertEquals(HttpStatus.NOT_FOUND, ex.statusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.DISCOUNT_NOT_FOUND.urn, response.error.code)
    }

    private fun url(id: Long) = "http://localhost:$port/v1/discounts/$id"
}
