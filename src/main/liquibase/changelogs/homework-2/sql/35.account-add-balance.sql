ALTER TABLE account
    ADD COLUMN balance decimal default 0 CHECK ( balance >= 0);