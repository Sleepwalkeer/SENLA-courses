ALTER TABLE order_item
    ADD CONSTRAINT fk_order_id
        FOREIGN KEY (order_id)
            REFERENCES rent_order (id) ON DELETE CASCADE ON UPDATE NO ACTION;