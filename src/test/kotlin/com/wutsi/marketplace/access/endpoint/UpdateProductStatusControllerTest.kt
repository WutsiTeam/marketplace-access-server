package com.wutsi.marketplace.access.endpoint

import com.wutsi.marketplace.access.dao.ProductRepository
import com.wutsi.marketplace.access.dto.UpdateProductStatusRequest
import com.wutsi.marketplace.access.enums.ProductStatus
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import org.springframework.web.client.RestTemplate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/UpdateProductStatusController.sql"])
class UpdateProductStatusControllerTest {
    @LocalServerPort
    val port: Int = 0

    val rest = RestTemplate()

    @Autowired
    private lateinit var dao: ProductRepository

    @Test
    fun publish() {
        val request = UpdateProductStatusRequest(
            status = ProductStatus.PUBLISHED.name
        )
        val response = rest.postForEntity(url(100), request, Any::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)

        val product = dao.findById(100).get()
        assertEquals(ProductStatus.PUBLISHED, product.status)
        assertNotNull(product.published)
    }

    @Test
    fun unpublish() {
        val request = UpdateProductStatusRequest(
            status = ProductStatus.DRAFT.name
        )
        val response = rest.postForEntity(url(101), request, Any::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)

        val product = dao.findById(101).get()
        assertEquals(ProductStatus.DRAFT, product.status)
        assertNull(product.published)
    }

    private fun url(productId: Long = 100L) =
        "http://localhost:$port/v1/products/$productId/status"
}
