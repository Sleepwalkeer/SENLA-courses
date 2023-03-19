CREATE TABLE order_item (
     order_id bigint,
     item_id bigint,
     PRIMARY KEY (order_id, item_id)
);