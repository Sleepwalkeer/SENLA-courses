--How can you produce a list of bookings on the day of 2012-09-14
--which will cost the member (or guest) more than $30?
--Remember that guests have different costs to members (the listed costs are per half-hour 'slot'),
--and the guest user is always ID 0. Include in your output the name of the facility,
--the name of the member formatted as a single column, and the cost. Order by descending cost.
CREATE TABLE cd.facilities
(
    facid      integer PRIMARY KEY,
    name       character varying(100) NOT NULL,
    membercost numeric                NOT NULL,
    guestcost  numeric                NOT NULL
);
insert into cd.facilities(facid, name, membercost, guestcost)
values (0, 'Tennis Court 1', 100, 70),
       (1, 'Football field', 200, 140),
       (2, 'Massage Room', 50, 100),
       (3, 'Tennis Court 2', 300, 200),
       (4, 'Tennis Court 2', 400, 300),
       (5, 'Table tennis', 150, 250),
       (6, 'Football field', 300, 220),
       (7, 'Massage Room', 800, 1000),
       (8, 'Tennis Court 2', 500, 400),
       (9, 'Tennis Court 2', 600, 500);

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
    facid     integer NOT NULL,
    memid     integer NOT NULL,
    starttime timestamp PRIMARY KEY,
    slots     integer NOT NULL
);
insert into cd.bookings(facid, memid, starttime, slots)
values (0, 4, '2012-09-14 01:11:11', 50),
       (3, 2, '1999-01-08 02:05:06', 20),
       (4, 1, '2012-09-14 03:01:01', 2),
       (2, 9, '2015-09-02 04:43:05', 14),
       (5, 0, '2012-09-14 05:12:55', 5),
       (1, 3, '2018-07-25 06:11:11', 22),
       (2, 2, '2012-09-21 07:05:06', 40),
       (3, 9, '2012-09-14 08:01:01', 180),
       (1, 0, '2012-09-14 09:43:05', 200),
       (5, 8, '2017-10-30 10:12:55', 250);
select member, facility, cost
from (select CONCAT(members.firstname, ' ', members.surname) as member,
             facilities.name                                 as facility,
             case
                 when members.memid = 0 then
                     bookings.slots * facilities.guestcost
                 else
                     bookings.slots * facilities.membercost
                 end                                         as cost
      from cd.members members
               inner join cd.bookings bookings
                          on members.memid = bookings.memid
               inner join cd.facilities facilities
                          on bookings.facid = facilities.facid
      where bookings.starttime between '2012-09-14 00:00:00' and '2012-09-14 23:59:59') as bookings
where cost > 30

order by cost desc;


