ALTER TABLE item
drop
CONSTRAINT fk_category,
    ADD CONSTRAINT fk_category
        FOREIGN KEY(category_id) REFERENCES category(id) ON DELETE
CASCADE ON UPDATE NO ACTION;