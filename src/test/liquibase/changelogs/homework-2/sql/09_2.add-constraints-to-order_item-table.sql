ALTER TABLE order_item
ADD CONSTRAINT fk_item_id FOREIGN KEY(item_id) REFERENCES item(id);