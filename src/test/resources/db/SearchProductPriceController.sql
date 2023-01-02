INSERT INTO T_STORE(id, account_id, product_count, published_product_count, currency, status)
    VALUES
        (1, 11, 0, 0, 'XAF', 1),
        (2, 21, 0, 0, 'XAF', 1),
        (3, 31, 0, 0, 'XAF', 1)
    ;

INSERT INTO T_PRODUCT(id, store_fk, category_fk, status, is_deleted, title, summary, description, price, currency, quantity, published, deleted)
    VALUES
        (100, 1, null, 2, false, 'TV', 'summary of TV', 'description of TV', 150000, 'XAF', 10, now(), null),
        (200, 2, null, 2, false, null, null, null, 2000, 'XAF', null, now(), null),
        (300, 3, null, 2, false, null, null, null, 300000, 'XAF', null, now(), null),
        (400, 4, null, 2, false, null, null, null, 300000, 'XAF', null, now(), null)
    ;

INSERT INTO T_DISCOUNT(id, store_fk, name, rate, starts, ends, all_products)
    VALUES
        (100, 1, 'FIN10', 10, date_add(now(), interval -1 day), date_add(now(), interval 30 day), true),
        (101, 1, 'FIN25', 25, date_add(now(), interval -1 day), date_add(now(), interval 3 day), true),
        (200, 2, 'FIN20', 20, date_add(now(), interval -1 day), date_add(now(), interval 3 day), false)
    ;

INSERT INTO T_DISCOUNT_PRODUCT(discount_fk, product_fk)
    VALUES
        (200, 200)
    ;
