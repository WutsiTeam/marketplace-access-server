package com.wutsi.marketplace.access.delegate

import com.wutsi.marketplace.access.dto.SaveCategoryRequest
import com.wutsi.marketplace.access.service.CategoryService
import com.wutsi.platform.core.logging.KVLogger
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest
import javax.transaction.Transactional

@Service
class SaveCategoryDelegate(
    private val service: CategoryService,
    private val httpRequest: HttpServletRequest,
    private val logger: KVLogger,
) {
    @Transactional
    fun invoke(id: Long, request: SaveCategoryRequest) {
        logger.add("request_title", request.title)
        logger.add("request_parent_id", request.parentId)

        val language = httpRequest.getHeader("Accept-Language")
        logger.add("language", language)

        service.save(id, request, language)
    }
}
