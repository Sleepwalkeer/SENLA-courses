--Produce a list of the total number of slots booked per facility in the month of September 2012.
--Produce an output table consisting of facility id and slots, sorted by the number of slots.
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
select facid, sum(slots)
from cd.bookings
where cd.bookings.starttime >= '2012-09-01'
  AND starttime < '2012-10-01'
group by facid
order by sum(slots);