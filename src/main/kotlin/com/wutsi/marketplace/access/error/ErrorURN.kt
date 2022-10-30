package com.wutsi.marketplace.access.error

enum class ErrorURN(val urn: String) {
    CATEGORY_NOT_FOUND("urn:wutsi:error:marketplace-access:category-not-found"),
    PARENT_CATEGORY_NOT_FOUND("urn:wutsi:error:marketplace-access:parent-category-not-found")
}
