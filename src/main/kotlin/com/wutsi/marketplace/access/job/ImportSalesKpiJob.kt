package com.wutsi.marketplace.access.job

import com.wutsi.marketplace.access.service.ProductService
import com.wutsi.platform.core.cron.AbstractCronJob
import com.wutsi.platform.core.cron.CronLockManager
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ImportSalesKpiJob(
    private val service: ProductService,
    lockManager: CronLockManager,
) : AbstractCronJob(lockManager) {
    override fun getJobName() = "import-sales-kpi"

    @Scheduled(cron = "\${wutsi.application.jobs.import-sales-kpi.cron}")
    override fun run() {
        super.run()
    }

    override fun doRun(): Long {
        return service.importSalesKpi(LocalDate.now())
    }
}
