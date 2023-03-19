ALTER TABLE rent_order
ADD COLUMN total_price decimal check ( total_price > 0 );