ALTER TABLE account
ADD COLUMN first_name varchar(25) NOT NULL,
ADD COLUMN second_name varchar(25) NOT NULL,
ADD COLUMN phone varchar(15) UNIQUE NOT NULL,
ADD COLUMN email varchar(64)  UNIQUE NOT NULL;
