ALTER TABLE item
    ADD COLUMN quantity int default 0 check ( quantity >= 0 );