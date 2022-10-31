INSERT INTO T_STORE(id, account_id, product_count, published_product_count, is_deleted, deleted, currency)
    VALUES
        (100, 1, 10, 5, false, null, 'XAF'),
        (199, 1, 0, 0, true, now(), 'XAF'),
        (200, 2, 10, 5, false, null, 'XAF'),
        (300, 3, 10, 5, false, null, 'XAF'),
        (400, 4, 10, 5, false, null, 'XAF')
    ;
