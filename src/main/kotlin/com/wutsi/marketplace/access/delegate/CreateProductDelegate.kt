package com.wutsi.marketplace.access.`delegate`

import com.wutsi.marketplace.access.dto.CreateProductRequest
import com.wutsi.marketplace.access.dto.CreateProductResponse
import com.wutsi.marketplace.access.service.ProductService
import org.springframework.stereotype.Service

@Service
public class CreateProductDelegate(private val service: ProductService) {
    public fun invoke(request: CreateProductRequest): CreateProductResponse {
        val product = service.create(request)
        return CreateProductResponse(
            productId = product.id ?: -1
        )
    }
}
