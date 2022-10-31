package com.wutsi.marketplace.access.service

import com.wutsi.marketplace.access.dao.ProductRepository
import com.wutsi.marketplace.access.dto.CreateProductRequest
import com.wutsi.marketplace.access.entity.ProductEntity
import com.wutsi.marketplace.access.enums.ProductStatus
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val dao: ProductRepository,
    private val categoryService: CategoryService,
    private val pictureService: PictureService,
    private val storeService: StoreService
) {
    fun create(request: CreateProductRequest): ProductEntity {
        val store = storeService.findById(request.storeId)
        val product = dao.save(
            ProductEntity(
                title = request.title,
                summary = request.summary,
                status = ProductStatus.DRAFT,
                price = request.price,
                category = request.categoryId?.let { categoryService.findById(it) },
                store = store,
                currency = store.currency
            )
        )

        product.thumbnail = pictureService.create(product, request.pictureUrl)
        return dao.save(product)
    }
}
