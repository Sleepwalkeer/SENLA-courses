ALTER TABLE rent_order
ADD CONSTRAINT fk_customer FOREIGN KEY(customer_id) REFERENCES account(id);
