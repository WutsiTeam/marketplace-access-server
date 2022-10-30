package com.wutsi.marketplace.access.endpoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.wutsi.marketplace.access.dao.CategoryRepository
import com.wutsi.marketplace.access.dto.SaveCategoryRequest
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
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/SaveCategoryController.sql"])
class SaveCategoryControllerTest : AbstractLanguageAwareControllerTest() {
    @LocalServerPort
    val port: Int = 0

    @Autowired
    private lateinit var dao: CategoryRepository

    @Test
    fun create() {
        // WHEN
        val request = SaveCategoryRequest(title = "New Category")
        val response = rest.postForEntity(url(1), request, Any::class.java)

        // THEN
        assertEquals(HttpStatus.OK, response.statusCode)

        val category = dao.findById(1L)
        assertTrue(category.isPresent)
        assertEquals(request.title, category.get().title)
        assertNull(category.get().titleFrench)
        assertNull(category.get().titleFrenchAscii)
        assertNull(category.get().parent)
    }

    @Test
    fun update() {
        // GIVEN
        language = "fr"

        // WHEN
        val request = SaveCategoryRequest(title = "Marketing/Publicité", parentId = 2000)
        val response = rest.postForEntity(url(1110), request, Any::class.java)

        // THEN
        assertEquals(HttpStatus.OK, response.statusCode)

        val category = dao.findById(1110L)
        assertTrue(category.isPresent)
        assertEquals("Computers", category.get().title)
        assertEquals(request.title, category.get().titleFrench)
        assertEquals("Marketing/Publicite", category.get().titleFrenchAscii)
        assertEquals(2000L, category.get().parent?.id)
    }

    @Test
    fun badParent() {
        // WHEN
        val request = SaveCategoryRequest(title = "Yo Man", parentId = 99999)
        val ex = assertThrows<HttpClientErrorException> {
            rest.postForEntity(url(1110), request, Any::class.java)
        }

        // THEN
        assertEquals(HttpStatus.NOT_FOUND, ex.statusCode)

        val response = ObjectMapper().readValue(ex.responseBodyAsString, ErrorResponse::class.java)
        assertEquals(ErrorURN.PARENT_CATEGORY_NOT_FOUND.urn, response.error.code)
    }

    private fun url(id: Long) = "http://localhost:$port/v1/categories/$id"
}
