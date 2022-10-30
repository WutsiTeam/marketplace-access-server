INSERT INTO T_STORE(id, account_id, product_count, published_product_count, is_deleted, deleted)
    VALUES
        (100, 1, 10, 5, false, null),
        (199, 1, 0, 0, true, now()),
        (200, 2, 10, 5, false, null),
        (300, 3, 10, 5, false, null),
        (400, 4, 10, 5, false, null)
    ;
