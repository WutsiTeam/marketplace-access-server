package com.wutsi.marketplace.access.job

import com.wutsi.marketplace.access.dao.ProductRepository
import com.wutsi.marketplace.access.endpoint.AbstractLanguageAwareControllerTest
import com.wutsi.platform.core.storage.StorageService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import java.io.ByteArrayInputStream
import java.time.LocalDate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = ["/db/clean.sql", "/db/ImportSalesKpiJob.sql"])
internal class ImportSalesKpiJobTest : AbstractLanguageAwareControllerTest() {
    @Autowired
    private lateinit var job: ImportSalesKpiJob

    @Autowired
    private lateinit var storage: StorageService

    @Autowired
    private lateinit var dao: ProductRepository

    @Test
    fun run() {
        // GIVEN
        val csv = """
            business_id,product_id,total_orders,total_units,total_value
            1,100,3,6,9000
            1,101,1,1,500
            2,200,1,1,1500
            9,9999,1,1,1500
        """.trimIndent()
        val date = LocalDate.now()
        val path = "kpi/${date.year}/${date.monthValue}/${date.dayOfMonth}/sales.csv"
        storage.store(path, ByteArrayInputStream(csv.toByteArray()))

        // WHEN
        job.run()

        // THEN
        assertKpi(100, 3, 6, 9000)
        assertKpi(101, 1, 1, 500)
        assertKpi(102, 0, 0, 0)
        assertKpi(103, 0, 0, 0)
        assertKpi(199, 0, 0, 0)
        assertKpi(200, 1, 1, 1500)
    }

    private fun assertKpi(id: Long, totalOrders: Long, totalUnits: Long, totalSales: Long) {
        val product = dao.findById(id).get()
        assertEquals(totalOrders, product.totalOrders)
        assertEquals(totalUnits, product.totalUnits)
        assertEquals(totalSales, product.totalSales)
    }
}
