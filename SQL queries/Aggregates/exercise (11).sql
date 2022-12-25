--Output the facility id that has the highest number of slots booked.
-- For bonus points, try a version without a LIMIT clause.
CREATE TABLE cd.bookings
(
    facid     integer NOT NULL,
    starttime timestamp PRIMARY KEY,
    slots     integer NOT NULL
);
insert into cd.bookings(facid, starttime, slots)
values (0, '2012-09-21 01:11:11', 50),
       (3, '1999-01-08 02:05:06', 20),
       (4, '2012-09-21 03:01:01', 2),
       (2, '2015-09-02 04:43:05', 14),
       (5, '2017-03-30 05:12:55', 5),
       (1, '2018-07-25 06:11:11', 22),
       (2, '2012-09-21 07:05:06', 40),
       (3, '2012-09-21 08:01:01', 180),
       (1, '2012-06-02 09:43:05', 200),
       (5, '2017-10-30 10:12:55', 250);
with sum as (select facid, sum(slots) as totalslots
             from cd.bookings
             group by facid)
select facid, totalslots
from sum
where totalslots = (select max(totalslots) from sum);