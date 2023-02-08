ALTER TABLE account
    ADD COLUMN email varchar(64) UNIQUE NOT NULL;