ALTER TABLE rent_order
ADD COLUMN customer_id integer NOT NULL,
ADD COLUMN worker_id integer NOT NULL,
ADD COLUMN start_datetime timestamp NOT NULL,
ADD COLUMN end_datetime timestamp NOT NULL,
ADD COLUMN total_price decimal NOT NULL,
ADD CONSTRAINT fk_customer FOREIGN KEY(customer_id) REFERENCES account(id),
ADD CONSTRAINT fk_worker FOREIGN KEY(worker_id) REFERENCES account(id);