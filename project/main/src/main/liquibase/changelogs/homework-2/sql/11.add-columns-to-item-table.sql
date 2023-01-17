ALTER TABLE item
ADD COLUMN category_id integer  NOT NULL,
ADD COLUMN name varchar(50) UNIQUE NOT NULL,
ADD COLUMN price decimal NOT NULL,
ADD COLUMN quantity smallint NOT NULL,
ADD CONSTRAINT fk_category FOREIGN KEY(category_id) REFERENCES category(id);