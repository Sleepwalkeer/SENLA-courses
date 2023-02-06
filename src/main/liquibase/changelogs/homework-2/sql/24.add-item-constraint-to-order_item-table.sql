ALTER TABLE order_item
    DROP CONSTRAINT  fk_item_id,
ADD CONSTRAINT fk_item_id FOREIGN KEY(item_id) REFERENCES item(id);