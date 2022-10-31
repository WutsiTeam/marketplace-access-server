INSERT INTO T_STORE(id, account_id, product_count, published_product_count, is_deleted, deleted, currency)
    VALUES
        (1, 1, 0, 0, false, null, 'XAF');

INSERT INTO T_CATEGORY(id, parent_fk, title, title_french)
    VALUES
        (1100, null, 'Electronics', 'Électronique'),
        (1110, 1100, 'Computers', 'Ordinateurs')
    ;
