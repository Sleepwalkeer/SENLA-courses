CREATE TABLE account (
    id bigserial primary key references credentials (id) on delete cascade on update no action unique
);
