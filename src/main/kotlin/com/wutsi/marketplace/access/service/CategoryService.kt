package com.wutsi.marketplace.access.service

import com.wutsi.marketplace.access.dao.CategoryRepository
import com.wutsi.marketplace.access.dto.Category
import com.wutsi.marketplace.access.dto.CategorySummary
import com.wutsi.marketplace.access.dto.SaveCategoryRequest
import com.wutsi.marketplace.access.dto.SearchCategoryRequest
import com.wutsi.marketplace.access.entity.CategoryEntity
import com.wutsi.marketplace.access.error.ErrorURN
import com.wutsi.marketplace.access.util.StringUtil
import com.wutsi.platform.core.error.Error
import com.wutsi.platform.core.error.Parameter
import com.wutsi.platform.core.error.ParameterType
import com.wutsi.platform.core.error.exception.NotFoundException
import org.springframework.stereotype.Service
import javax.persistence.EntityManager
import javax.persistence.Query

@Service
public class CategoryService(
    private val dao: CategoryRepository,
    private val em: EntityManager,
) {
    fun findById(id: Long): CategoryEntity =
        dao.findById(id)
            .orElseThrow {
                NotFoundException(
                    error = Error(
                        code = ErrorURN.CATEGORY_NOT_FOUND.urn,
                        parameter = Parameter(
                            name = "id",
                            value = id,
                            type = ParameterType.PARAMETER_TYPE_PATH,
                        ),
                    ),
                )
            }

    fun save(id: Long, request: SaveCategoryRequest, language: String?): CategoryEntity {
        val category = dao.findById(id)
            .orElse(CategoryEntity(id = id))

        when (language?.lowercase()) {
            "fr" -> {
                category.titleFrench = request.title
                category.titleFrenchAscii = StringUtil.toAscii(request.title)
            }
            else -> category.title = request.title
        }
        category.parent = request.parentId?.let {
            dao.findById(request.parentId)
                .orElseThrow {
                    NotFoundException(
                        error = Error(
                            code = ErrorURN.PARENT_CATEGORY_NOT_FOUND.urn,
                            parameter = Parameter(
                                name = "parentId",
                                value = request.parentId,
                                type = ParameterType.PARAMETER_TYPE_PAYLOAD,
                            ),
                        ),
                    )
                }
        }
        return dao.save(category)
    }

    fun toCategory(category: CategoryEntity, language: String?) = Category(
        id = category.id,
        title = getTitle(category, language),
        parentId = category.parent?.id,
    )

    fun toCategorySummary(category: CategoryEntity, language: String?) = CategorySummary(
        id = category.id,
        title = getTitle(category, language),
        parentId = category.parent?.id,
    )

    private fun getTitle(category: CategoryEntity, language: String?) =
        when (language?.lowercase()) {
            "fr" -> category.titleFrench ?: category.title
            else -> category.title
        }

    fun search(request: SearchCategoryRequest, language: String?): List<CategoryEntity> {
        val query = em.createQuery(sql(request, language))
        parameters(request, query)
        return query
            .setFirstResult(request.offset)
            .setMaxResults(request.limit)
            .resultList as List<CategoryEntity>
    }

    private fun sql(request: SearchCategoryRequest, language: String?): String {
        val select = select()
        val where = where(request, language)
        val orderBy = orderBy(language)
        return if (where.isNullOrEmpty()) {
            select
        } else {
            "$select WHERE $where $orderBy"
        }
    }

    private fun select(): String =
        "SELECT a FROM CategoryEntity a"

    private fun orderBy(language: String?): String =
        when (language?.lowercase()) {
            "fr" -> "ORDER BY a.titleFrench"
            else -> "ORDER BY a.title"
        }

    private fun where(request: SearchCategoryRequest, language: String?): String {
        val criteria = mutableListOf<String>()

        if (request.topCategories == true) {
            criteria.add("a.parent IS NULL")
        }
        if (request.categoryIds.isNotEmpty()) {
            criteria.add("a.id IN :category_ids")
        }
        if (!request.keyword.isNullOrEmpty()) {
            when (language?.lowercase()) {
                "fr" -> criteria.add("((a.titleFrenchAscii IS NULL AND UCASE(a.title) LIKE :keyword) OR (UCASE(a.titleFrenchAscii) LIKE :keyword))")
                else -> criteria.add("UCASE(a.title) LIKE :keyword")
            }
        }
        return criteria.joinToString(separator = " AND ")
    }

    private fun parameters(request: SearchCategoryRequest, query: Query) {
        if (request.categoryIds.isNotEmpty()) {
            query.setParameter("category_ids", request.categoryIds)
        }
        if (!request.keyword.isNullOrEmpty()) {
            query.setParameter("keyword", StringUtil.toAscii(request.keyword).uppercase() + "%")
        }
    }
}
