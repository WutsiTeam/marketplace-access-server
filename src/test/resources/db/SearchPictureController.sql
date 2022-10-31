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

INSERT INTO T_PRODUCT(id, store_fk, category_fk, status, is_deleted, title, summary, description, price, comparable_price, currency, quantity, published, deleted)
    VALUES
        (100, 1, 1110, 2, false, 'TV', 'summary of TV', 'description of TV', 150000, 200000, 'XAF', 10, now(), null),
        (200, 1, 1110, 1, false, 'TV', 'Sample TV', 'Long description', 50000, null, 'XAF', null, null, null),
        (999, 1, 1110, 1, true, 'TV', 'Sample TV', 'Long description', 50000, null, 'XAF', null, null, now())
    ;

INSERT INTO T_PICTURE(id, product_fk, url, hash, is_deleted, deleted)
    VALUES
        (101, 100, 'https://www.img.com/101.png', '9f44c2e86d6c7d370e44e35952b4df1e', false, null),
        (102, 100, 'https://www.img.com/102.png', 'fe86f394f7a5649695daa2bcf34b17c5', false, null),
        (199, 100, 'https://www.img.com/199.png', '3a77dfc083764cfe3a67d18fe4957821', true, now()),

        (200, 200, 'https://www.img.com/200.png', '8d819d5bb8f383fc725ad55032621c2f', false, null),
        (299, 200, 'https://www.img.com/299.png', '0589a915560da7b4eec5f2fb4f3b719c', true, now()),

        (999, 999, 'https://www.img.com/999.png', '0df41eb610184c8d855503fa94e5d3a1', false, null)
    ;
