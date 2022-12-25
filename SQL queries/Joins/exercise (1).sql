--How can you produce a list of the start times for bookings by members named 'David Farrell'?
CREATE TABLE cd.members
(
    memid     integer PRIMARY KEY,
    surname   varchar(100) NOT NULL,
    firstname varchar(100) NOT NULL
);
insert into cd.members(memid, firstname, surname)
values (0, 'David', 'Farrell'),
       (1, 'Jones', 'Douglas'),
       (2, 'Rumney', 'Henrietta'),
       (3, 'Purview', 'Henry'),
       (4, 'Crumpet', 'Erica');

CREATE TABLE cd.bookings
(
    memid     integer NOT NULL,
    starttime timestamp PRIMARY KEY
);
insert into cd.bookings(memid, starttime)
values (0, '2018-12-25 11:11:11'),
       (3, '1999-01-08 04:05:06'),
       (4, '2012-09-01 01:01:01'),
       (0, '2015-09-02 18:43:05'),
       (0, '2017-03-30 17:12:55');


SELECT starttime
FROM cd.members
         INNER JOIN cd.bookings
                    ON cd.members.memid = cd.bookings.memid
where members.firstname = 'David'
  and members.surname = 'Farrell';
