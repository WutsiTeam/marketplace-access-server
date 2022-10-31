INSERT INTO T_STORE(id, account_id, product_count, published_product_count, is_deleted, deleted, currency)
    VALUES
        (1, 1, 0, 0, false, null, 'XAF'),
        (2, 2, 3, 1, false, null, 'XAF')
    ;

INSERT INTO T_CATEGORY(id, parent_fk, title, title_french)
    VALUES
        (1100, null, 'Electronics', 'Électronique'),
        (1110, 1100, 'Computers', 'Ordinateurs')
    ;

INSERT INTO T_PRODUCT(id, store_fk, status)
    VALUES
        (201, 2, 1),
        (202, 2, 1),
        (203, 2, 2)
    ;
