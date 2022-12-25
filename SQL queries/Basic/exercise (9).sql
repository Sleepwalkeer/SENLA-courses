--How can you produce an ordered list of the first 10 surnames in the members table?
-- The list must not contain duplicates.
CREATE TABLE cd.members
(
    memid   integer PRIMARY KEY,
    surname varchar(100) NOT NULL
);
insert into cd.members(memid, surname)
values (0, 'Sarwin'),
       (1, 'Jones'),
       (2, 'Sarwin'),
       (3, 'Purview'),
       (4, 'Crumpet'),
       (5, 'Jones'),
       (6, 'Schmuck'),
       (7, 'Holmes'),
       (8, 'Maslow'),
       (9, 'Holland'),
       (10, 'Pence'),
       (11, 'Biden'),
       (12, 'Bobbie'),
       (13, 'Coats'),
       (14, 'Chattem'),
       (15, 'Schmuck'),
       (16, 'Schmuck');
SELECT DISTINCT(surname)
FROM cd.members
ORDER BY surname
LIMIT 10;
