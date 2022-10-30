package com.wutsi.marketplace.access.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.marketplace.access.dao.StoreRepository
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
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/DeleteStoreController.sql"])
public class DeleteStoreControllerTest {
    @LocalServerPort
    val port: Int = 0

    private val rest = RestTemplate()

    @Autowired
    private lateinit var dao: StoreRepository

    @Test
    fun delete() {
        rest.delete(url(100))

        val store = dao.findById(100).get()
        assertTrue(store.isDeleted)
        assertNotNull(store.deleted)
    }

    @Test
    fun notFound() {
        val ex = assertThrows<HttpClientErrorException> {
            rest.delete(url(9999999))
        }

        // THEN
        assertEquals(HttpStatus.NOT_FOUND, ex.statusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.STORE_NOT_FOUND.urn, response.error.code)
    }

    @Test
    fun deleted() {
        val ex = assertThrows<HttpClientErrorException> {
            rest.delete(url(199))
        }

        // THEN
        assertEquals(HttpStatus.NOT_FOUND, ex.statusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.STORE_DELETED.urn, response.error.code)
    }

    private fun url(id: Long) = "http://localhost:$port/v1/stores/$id"
}
