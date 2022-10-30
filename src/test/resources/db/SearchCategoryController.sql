INSERT INTO T_CATEGORY(id, parent_fk, title, title_french, title_french_ascii)
    VALUES
        (1100, null, 'Electronics', 'Électronique', null),
        (1110, 1100, 'Computers', 'Ordinateurs', null),
        (1111, 1100, 'Laptops', 'Portables', null),
        (1112, 1100, 'Tablets', 'Tablettes', null),
        (1114, 1100, 'Monitors', 'Écrans', 'Ecrans'),
        (1115, 1100, 'Printers', 'Imprimantes', null),
        (1116, 1100, 'Routers', 'Routeurs', null),
        (1117, 1100, 'Projectors', 'Projecteurs', null),
        (1130, 1100, 'Cameras', 'Appareil Photos', null),
        (1140, 1100, 'TV', 'TV', null),
        (1141, 1100, 'Speakers', 'Haut-Parleurs', null),
        (1150, 1100, 'Cell Phones', 'Téléphones portables', 'Telephones portables'),
        (1151, 1100, 'Headphones', 'Écouteurs', 'Ecouteurs'),
        (1152, 1100, 'Cases and Covers', 'Étuis et Housses', 'Etuis et Housses'),
        (1153, 1100, 'SIM Cards', 'Cartes SIM', null),
        (1154, 1100, 'Chargers', 'Chargeurs', null),
        (1160, 1100, 'Electronic Accessories', 'Accessoires Électronique', 'Accessoires Electronique'),

        (1200, null, 'Beauty', 'Beauté', 'Beaute'),
        (1210, 1200, 'Makeup', 'Maquillage', null),
        (1220, 1200, 'Nails', 'Ongles', null)
    ;
