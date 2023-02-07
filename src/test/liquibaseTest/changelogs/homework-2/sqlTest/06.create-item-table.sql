CREATE TABLE item
(
    id          serial primary key,
    category_id integer            NOT NULL,
    name        varchar(50) UNIQUE NOT NULL,
    price       decimal            NOT NULL,
    quantity    smallint           NOT NULL
);