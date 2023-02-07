CREATE TABLE credentials (
     id serial primary key,
     username varchar(30) UNIQUE NOT NULL,
      password varchar(30) NOT NULL
);