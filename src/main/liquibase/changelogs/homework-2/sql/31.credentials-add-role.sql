ALTER TABLE credentials
ADD COLUMN role varchar(30) NOT NULL DEFAULT 'USER';