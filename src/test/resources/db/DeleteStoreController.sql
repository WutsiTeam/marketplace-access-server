INSERT INTO T_STORE(id, account_id, product_count, published_product_count, is_deleted, deleted, currency)
    VALUES
        (100, 100, 10, 5, false, null, 'XAF'),
        (199, 100, 0, 0, true, now(), 'XAF')
    ;
