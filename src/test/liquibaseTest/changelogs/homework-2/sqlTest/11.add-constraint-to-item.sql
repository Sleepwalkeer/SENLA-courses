ALTER TABLE item
ADD CONSTRAINT fk_category FOREIGN KEY(category_id) REFERENCES category(id);