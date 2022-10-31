package com.wutsi.marketplace.access.error

enum class ErrorURN(val urn: String) {
    ATTRIBUTE_NOT_VALID("urn:wutsi:error:marketplace-access:attribute-not-valid"),

    CATEGORY_NOT_FOUND("urn:wutsi:error:marketplace-access:category-not-found"),
    PARENT_CATEGORY_NOT_FOUND("urn:wutsi:error:marketplace-access:parent-category-not-found"),

    PICTURE_NOT_FOUND("urn:wutsi:error:marketplace-access:picture-not-found"),
    PICTURE_DELETED("urn:wutsi:error:marketplace-access:picture-deleted"),

    PRODUCT_NOT_FOUND("urn:wutsi:error:marketplace-access:product-not-found"),
    PRODUCT_DELETED("urn:wutsi:error:marketplace-access:product-deleted"),

    STATUS_NOT_VALID("urn:wutsi:error:marketplace-access:status-not-valid"),

    STORE_NOT_FOUND("urn:wutsi:error:marketplace-access:store-not-found"),
    STORE_DELETED("urn:wutsi:error:marketplace-access:store-deleted")
}
