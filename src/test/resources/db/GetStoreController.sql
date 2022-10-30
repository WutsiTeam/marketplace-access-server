INSERT INTO T_STORE(id, account_id, product_count, published_product_count, is_deleted, deleted)
    VALUES
        (100, 100, 10, 5, false, null),
        (199, 100, 0, 0, true, now())
    ;
