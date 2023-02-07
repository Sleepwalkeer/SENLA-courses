ALTER TABLE rent_order
ADD CONSTRAINT fk_worker FOREIGN KEY(worker_id) REFERENCES account(id);