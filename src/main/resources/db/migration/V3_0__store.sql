CREATE TABLE T_STORE(
    id              BIGINT NOT NULL,

    product_count           INT NOT NULL DEFAULT 0,
    published_product_count INT NOT NULL DEFAULT 0

    PRIMARY KEY (id)
);
