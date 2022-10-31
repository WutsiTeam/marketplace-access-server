package com.wutsi.marketplace.access.service

import com.wutsi.marketplace.access.dao.PictureRepository
import com.wutsi.marketplace.access.dto.PictureSummary
import com.wutsi.marketplace.access.entity.PictureEntity
import com.wutsi.marketplace.access.entity.ProductEntity
import com.wutsi.marketplace.access.error.ErrorURN
import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.exception.NotFoundException
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

    fun findById(id: Long): PictureEntity {
        val picture = dao.findById(id)
            .orElseThrow {
                NotFoundException(
                    error = Error(
                        code = ErrorURN.PICTURE_NOT_FOUND.urn,
                        parameter = Parameter(
                            name = "id",
                            value = id
                        )
                    )
                )
            }
        if (picture.isDeleted) {
            throw NotFoundException(
                error = Error(
                    code = ErrorURN.PICTURE_DELETED.urn,
                    parameter = Parameter(
                        name = "id",
                        value = id
                    )
                )
            )
        }
        return picture
    }

    fun toPictureSummary(picture: PictureEntity) = PictureSummary(
        id = picture.id ?: -1,
        url = picture.url
    )

    private fun hash(url: String): String =
        DigestUtils.md5Hex(url.lowercase()).lowercase()
}
