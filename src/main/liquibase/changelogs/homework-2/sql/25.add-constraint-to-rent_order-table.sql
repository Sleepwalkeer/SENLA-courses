ALTER TABLE rent_order
DROP COLUMN total_price,
ADD COLUMN total_price decimal,
ADD CONSTRAINT fk_customer FOREIGN KEY(customer_id) REFERENCES account(id),
ADD CONSTRAINT fk_worker FOREIGN KEY(worker_id) REFERENCES account(id);