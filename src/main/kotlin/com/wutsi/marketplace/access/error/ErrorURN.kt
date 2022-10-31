package com.wutsi.marketplace.access.error

enum class ErrorURN(val urn: String) {
    CATEGORY_NOT_FOUND("urn:wutsi:error:marketplace-access:category-not-found"),
    PARENT_CATEGORY_NOT_FOUND("urn:wutsi:error:marketplace-access:parent-category-not-found"),

    PRODUCT_NOT_FOUND("urn:wutsi:error:marketplace-access:product-not-found"),
    PRODUCT_DELETED("urn:wutsi:error:marketplace-access:product-deleted"),

    STORE_NOT_FOUND("urn:wutsi:error:marketplace-access:store-not-found"),
    STORE_DELETED("urn:wutsi:error:marketplace-access:store-deleted")
}
