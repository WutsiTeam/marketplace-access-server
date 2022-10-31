package com.wutsi.marketplace.access.service

import com.wutsi.marketplace.access.dao.PictureRepository
import com.wutsi.marketplace.access.entity.PictureEntity
import com.wutsi.marketplace.access.entity.ProductEntity
import org.apache.commons.codec.digest.DigestUtils
import org.springframework.stereotype.Service

@Service
class PictureService(
    private val dao: PictureRepository
) {
    fun create(product: ProductEntity, url: String): PictureEntity =
        dao.save(
            PictureEntity(
                product = product,
                url = url.lowercase(),
                hash = hash(url)
            )
        )

    private fun hash(url: String): String =
        DigestUtils.md5Hex(url.lowercase()).lowercase()
}
