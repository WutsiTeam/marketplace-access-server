INSERT INTO T_STORE(id, account_id, product_count, published_product_count, is_deleted, deleted, currency)
    VALUES
        (1, 1, 3, 1, false, null, 'XAF')
    ;

INSERT INTO T_PRODUCT(id, store_fk, status, is_deleted, deleted)
    VALUES
        (100, 1, 1, false, null),
        (101, 1, 1, false, null),
        (102, 1, 2, false, null),
        (199, 1, 1, true, now())
    ;
