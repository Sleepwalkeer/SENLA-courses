CREATE TABLE account
(
    id serial primary key references credentials (id) on delete cascade on update no action unique,
     first_name varchar(25) NOT NULL,
     second_name varchar(25) NOT NULL,
     phone varchar(15) UNIQUE NOT NULL,
     email varchar(64)  UNIQUE NOT NULL
);
