CREATE TABLE credentials (
     id integer primary key REFERENCES account(id) UNIQUE
);