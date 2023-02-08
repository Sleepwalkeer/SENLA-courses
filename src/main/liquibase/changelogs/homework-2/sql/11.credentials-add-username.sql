ALTER TABLE credentials
ADD COLUMN username varchar(30) UNIQUE NOT NULL;