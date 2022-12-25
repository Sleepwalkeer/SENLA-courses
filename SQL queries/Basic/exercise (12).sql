--You'd like to get the first and last name of the last member(s) who signed up - not just the date.
CREATE TABLE cd.members
(
    memid     integer PRIMARY KEY,
    surname   varchar(100) NOT NULL,
    firstname varchar(100) NOT NULL,
    joindate  timestamp NOT NULL
);
insert into cd.members(memid, surname, firstname, joindate)
values (0, 'Sarwin', 'Ramnaresh', '1999-01-08 04:05:06'),
       (1, 'Jones', 'Douglas', '2012-09-02 18:43:05'),
       (2, 'Rumney', 'Henrietta', '2012-09-01 01:01:01'),
       (3, 'Purview', 'Henry', '2012-08-01 21:11:32'),
       (4, 'Crumpet', 'Erica', '2018-12-25 11:11:11');
SELECT firstname, surname, joindate
FROM cd.members
WHERE joindate = (SELECT MAX(joindate)
                  FROM cd.members);
