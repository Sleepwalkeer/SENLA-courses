--You'd like to get the signup date of your last member. How can you retrieve this information?
CREATE TABLE cd.members
(
    memid     integer PRIMARY KEY,
    joindate  timestamp NOT NULL
);
insert into cd.members(memid, joindate)
values (0, '1999-01-08 04:05:06'),
       (1, '2012-09-02 18:43:05'),
       (2, '2012-09-01 01:01:01'),
       (3, '2012-08-01 21:11:32'),
       (4, '2018-12-25 11:11:11');
SELECT MAX(joindate) as latest
FROM cd.members;
