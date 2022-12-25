--How can you produce a list of all members who have used a tennis court?
-- Include in your output the name of the court, and the name of the member formatted as a single column.
-- Ensure no duplicate data, and order by the member name followed by the facility name.
CREATE TABLE cd.facilities
(
    facid integer PRIMARY KEY,
    name  varchar(100) NOT NULL
);
insert into cd.facilities(facid, name)
values (0, 'Tennis Court 1'),
       (1, 'Football field'),
       (2, 'Massage Room'),
       (3, 'Tennis Court 2'),
       (4, 'Tennis Court 2'),
       (5, 'Table tennis');

CREATE TABLE cd.members
(
    memid     integer PRIMARY KEY,
    surname   varchar(100) NOT NULL,
    firstname varchar(100) NOT NULL
);
insert into cd.members(memid, firstname, surname)
values (0, 'Sarwin', 'Ramnaresh'),
       (1, 'Jones', 'Douglas'),
       (2, 'Rumney', 'Henrietta'),
       (3, 'Purview', 'Henry'),
       (4, 'Crumpet', 'Erica'),
       (5, 'Carlos', 'Chavez'),
       (6, 'Tony', 'Montana'),
       (7, 'Another', 'Person'),
       (8, 'Chuck', 'Wood'),
       (9, 'Peter', 'Piper');


CREATE TABLE cd.bookings
(
    facid     integer NOT NULL,
    memid     integer NOT NULL,
    starttime timestamp PRIMARY KEY
);
insert into cd.bookings(facid, memid, starttime)
values (0, 4, '2012-09-21 01:11:11'),
       (3, 2, '1999-01-08 02:05:06'),
       (4, 1, '2012-09-21 03:01:01'),
       (2, 9,'2015-09-02 04:43:05'),
       (5, 0,'2017-03-30 05:12:55'),
       (1, 3, '2018-07-25 06:11:11'),
       (2, 2,'2012-09-21 07:05:06'),
       (3, 9,'2012-09-21 08:01:01'),
       (1, 0,'2012-06-02 09:43:05'),
       (5, 8,'2017-10-30 10:12:55');

select distinct  CONCAT(members.firstname,' ',members.surname) as member, facilities.name as facility
from
    cd.members members
        inner join cd.bookings bookings
                   on members.memid = bookings.memid
        inner join cd.facilities facilities
                   on bookings.facid = facilities.facid
where
        facilities.name LIKE 'Tennis Court _'
order by member, facility