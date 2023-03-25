ALTER TABLE item
    ADD COLUMN discount decimal default 0 CHECK ( discount >= 0 AND discount < 100);