package com.wutsi.marketplace.access.endpoint

import com.wutsi.marketplace.access.dto.SearchOfferRequest
import com.wutsi.marketplace.access.dto.SearchOfferResponse
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/SearchOfferController.sql"])
public class SearchOfferControllerTest : AbstractLanguageAwareControllerTest() {
    @LocalServerPort
    public val port: Int = 0

    @Test
    public fun invoke() {
        val request = SearchOfferRequest(
            storeId = 1L,
        )
        val response = rest.postForEntity(url(), request, SearchOfferResponse::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)

        val offers = response.body!!.offers
        assertEquals(2, offers.size)
    }

    private fun url() = "http://localhost:$port/v1/offers/search"
}
