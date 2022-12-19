package com.wutsi.marketplace.access.`delegate`

import com.wutsi.marketplace.access.dto.CreateFileRequest
import com.wutsi.marketplace.access.dto.CreateFileResponse
import com.wutsi.marketplace.access.service.FileService
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
public class CreateFileDelegate(
    private val service: FileService
) {
    @Transactional
    public fun invoke(request: CreateFileRequest): CreateFileResponse {
        val file = service.add(request)
        return CreateFileResponse(
            fileId = file.id ?: -1
        )
    }
}
