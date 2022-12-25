--How can you produce a list of members who joined after the start of September 2012?
-- Return the memid, surname, firstname, and joindate of the members in question.
CREATE TABLE cd.members
(
    memid     integer PRIMARY KEY,
    surname   varchar(100) NOT NULL,
    firstname varchar(100) NOT NULL,
    joindate  timestamp
);
insert into cd.members(memid, surname, firstname, joindate)
values (0, 'Sarwin', 'Ramnaresh', '1999-01-08 04:05:06'),
       (1, 'Jones', 'Douglas', '2012-09-02 18:43:05'),
       (2, 'Rumney', 'Henrietta', '2012-09-01 01:01:01'),
       (3, 'Purview', 'Henry', '2012-08-01 21:11:32'),
       (4, 'Crumpet', 'Erica', '2018-12-25 11:11:11');
SELECT memid, surname, firstname, joindate
FROM cd.members
WHERE joindate > '2012-09-01'
