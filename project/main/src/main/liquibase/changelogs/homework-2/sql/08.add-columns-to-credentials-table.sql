ALTER TABLE credentials
ADD COLUMN username varchar(30) UNIQUE NOT NULL,
ADD COLUMN password varchar(30) NOT NULL;
