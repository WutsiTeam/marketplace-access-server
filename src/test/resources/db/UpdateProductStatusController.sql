INSERT INTO T_STORE(id, account_id, product_count, published_product_count, is_deleted, deleted, currency)
    VALUES
        (1, 1, 2, 1, false, null, 'XAF'),
        (2, 1, 4, 2, false, null, 'XAF')
    ;

INSERT INTO T_PRODUCT(id, store_fk, category_fk, status, is_deleted, title, summary, description, price, comparable_price, currency, quantity, published, deleted)
    VALUES
        (100, 1, null, 1, false, 'TV', 'summary of TV', 'description of TV', 150000, 200000, 'XAF', 10, null, null),
        (101, 1, null, 2, false, 'TV', 'summary of TV', 'description of TV', 150000, 200000, 'XAF', 10, now(), null),

        (200, 2, null, 2, false, 'TV', 'summary of TV', 'description of TV', 150000, 200000, 'XAF', 10, null, null),
        (201, 2, null, 2, false, 'TV', 'summary of TV', 'description of TV', 150000, 200000, 'XAF', 10, now(), null),
        (202, 2, null, 1, false, 'TV', 'summary of TV', 'description of TV', 150000, 200000, 'XAF', 10, null, null),
        (203, 2, null, 1, false, 'TV', 'summary of TV', 'description of TV', 150000, 200000, 'XAF', 10, now(), null)
    ;
