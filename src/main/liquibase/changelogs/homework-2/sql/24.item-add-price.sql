ALTER TABLE item
    ADD COLUMN price decimal check ( price > 0 );