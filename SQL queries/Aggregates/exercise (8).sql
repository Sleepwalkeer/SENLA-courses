--Produce a list of facilities with more than 1000 slots booked.
--Produce an output table consisting of facility id and slots, sorted by facility id.
CREATE TABLE cd.bookings
(
    facid     integer NOT NULL,
    starttime timestamp PRIMARY KEY,
    slots     integer NOT NULL
);
insert into cd.bookings(facid, starttime, slots)
values (0, '2012-09-21 01:11:11', 500),
       (3, '1999-01-08 02:05:06', 1200),
       (4, '2012-09-21 03:01:01', 780),
       (2, '2015-09-02 04:43:05', 190),
       (5, '2017-03-30 05:12:55', 900),
       (1, '2018-07-25 06:11:11', 220),
       (2, '2012-09-21 07:05:06', 400),
       (3, '2012-09-21 08:01:01', 180),
       (1, '2012-06-02 09:43:05', 800),
       (5, '2017-10-30 10:12:55', 250);
select facid, sum(slots)
from cd.bookings
group by facid
having sum(slots) > 1000
order by facid
