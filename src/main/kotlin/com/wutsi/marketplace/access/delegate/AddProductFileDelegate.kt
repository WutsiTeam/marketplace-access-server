package com.wutsi.marketplace.access.`delegate`

import com.wutsi.marketplace.access.dto.AddProductFileRequest
import com.wutsi.marketplace.access.dto.AddProductFileResponse
import com.wutsi.marketplace.access.service.FileService
import com.wutsi.marketplace.access.service.ProductService
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
public class AddProductFileDelegate(
    private val productService: ProductService,
    private val service: FileService
) {
    @Transactional
    public fun invoke(id: Long, request: AddProductFileRequest): AddProductFileResponse {
        val product = productService.findById(id)
        val file = service.add(product, request)
        return AddProductFileResponse(
            fileId = file.id ?: -1
        )
    }
}
