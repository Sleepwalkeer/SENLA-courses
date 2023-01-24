ALTER TABLE order_item
ADD CONSTRAINT fk_order_id FOREIGN KEY(order_id) REFERENCES rent_order(id),
ADD CONSTRAINT fk_item_id FOREIGN KEY(item_id) REFERENCES item(id);