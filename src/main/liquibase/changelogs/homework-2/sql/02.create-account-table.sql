CREATE TABLE account (
    id serial primary key references credentials (id) on delete cascade on update no action unique
);
