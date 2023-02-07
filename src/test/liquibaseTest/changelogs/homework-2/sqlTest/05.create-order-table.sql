CREATE TABLE rent_order
(
    id             serial primary key,
    customer_id    integer   NOT NULL,
    worker_id      integer   NOT NULL,
    start_datetime timestamp NOT NULL,
    end_datetime   timestamp NOT NULL,
    total_price    decimal   NOT NULL

);