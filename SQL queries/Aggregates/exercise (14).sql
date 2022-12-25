--Produce a list of each member name, id, and their first booking after September 1st 2012. Order by member ID.
CREATE TABLE cd.members
(
    memid     integer PRIMARY KEY,
    surname   character varying(100) NOT NULL,
    firstname character varying(100) NOT NULL
);
insert into cd.members(memid, surname, firstname)
values (0, 'GUEST', 'GUEST'),
       (1, 'Jones', 'Douglas'),
       (2, 'Rumney', 'Henrietta'),
       (3, 'Purview', 'Henry'),
       (4, 'Crumpet', 'Erica'),
       (5, 'Chavez', 'Carlos'),
       (6, 'Montana', 'Tony'),
       (7, 'Person', 'Another'),
       (8, 'Wood', 'Chuck'),
       (9, 'Piper', 'Peter');

CREATE TABLE cd.bookings
(
    memid     integer NOT NULL,
    starttime timestamp PRIMARY KEY,
    slots     integer NOT NULL
);
insert into cd.bookings(memid, starttime, slots)
values (4, '2012-09-21 01:11:11', 50),
       (2, '1999-01-08 02:05:06', 20),
       (1, '2013-09-21 03:01:01', 2),
       (9, '2015-09-02 04:43:05', 14),
       (0, '2017-03-30 05:12:55', 5),
       (3, '2018-07-25 06:11:11', 22),
       (2, '2014-09-21 07:05:06', 40),
       (9, '2015-09-21 08:01:01', 180),
       (0, '2012-06-02 09:43:05', 200),
       (8, '2017-10-30 10:12:55', 250);

select members.surname, members.firstname, members.memid, min(bookings.starttime) as starttime
from cd.bookings bookings
         inner join cd.members members on
    members.memid = bookings.memid
where starttime >= '2012-09-01'
group by members.surname, members.firstname, members.memid
order by members.memid;   