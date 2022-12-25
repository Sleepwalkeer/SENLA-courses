--Produce a list of facilities with a total revenue less than 1000.
-- Produce an output table consisting of facility name and revenue, sorted by revenue.
-- Remember that there's a different cost for guests and members!
CREATE TABLE cd.facilities
(
    facid      integer PRIMARY KEY,
    name       character varying(100) NOT NULL,
    membercost numeric                NOT NULL,
    guestcost  numeric                NOT NULL
);
insert into cd.facilities(facid, name, membercost, guestcost)
values (0, 'Tennis Court 1', 10, 70),
       (1, 'Football field', 20, 14),
       (2, 'Massage Room 1', 50, 100),
       (3, 'Tennis Court 2', 30, 20),
       (4, 'Tennis Court 3', 40, 30),
       (5, 'Table tennis', 150, 250),
       (6, 'Football field', 300, 220),
       (7, 'Massage Room 2', 80, 100),
       (8, 'Tennis Court 4', 50, 40),
       (9, 'Tennis Court 5', 60, 50);


CREATE TABLE cd.bookings
(
    facid     integer NOT NULL,
    memid     integer NOT NULL,
    starttime timestamp PRIMARY KEY,
    slots     integer NOT NULL
);
insert into cd.bookings(facid, memid, starttime, slots)
values (0, 4, '2012-09-21 01:11:11', 50),
       (3, 2, '1999-01-08 02:05:06', 20),
       (4, 1, '2012-09-21 03:01:01', 2),
       (2, 9, '2015-09-02 04:43:05', 14),
       (5, 0, '2017-03-30 05:12:55', 5),
       (1, 3, '2018-07-25 06:11:11', 22),
       (2, 2, '2012-09-21 07:05:06', 40),
       (3, 9, '2012-09-21 08:01:01', 180),
       (1, 0, '2012-06-02 09:43:05', 200),
       (5, 8, '2017-10-30 10:12:55', 250);
select name, "total revenue"
from (select facilities.name,
             sum(case
                     when memid = 0 then slots * facilities.guestcost
                     else slots * membercost
                 end) as "total revenue"
      from cd.bookings bookings
               inner join cd.facilities facilities
                          on bookings.facid = facilities.facid
      group by facilities.name) as revenue
where "total revenue" < 1000
order by "total revenue";