--Produce a list of the total number of slots booked per facility per month in the year of 2012.
-- Produce an output table consisting of facility id and slots, sorted by the id and month.
CREATE TABLE cd.bookings
(
    facid     integer NOT NULL,
    starttime timestamp PRIMARY KEY,
    slots     integer NOT NULL
);
insert into cd.bookings(facid, starttime, slots)
values (0, '2012-01-21 01:11:11', 50),
       (3, '1999-01-08 02:05:06', 20),
       (4, '2012-02-21 03:01:01', 2),
       (2, '2015-09-02 04:43:05', 14),
       (5, '2017-03-30 05:12:55', 5),
       (1, '2018-07-25 06:11:11', 22),
       (2, '2012-03-21 07:05:06', 40),
       (3, '2012-02-21 08:01:01', 180),
       (1, '2012-03-02 09:43:05', 200),
       (5, '2017-10-30 10:12:55', 250),
       (4, '2012-08-21 11:01:01', 72),
       (2, '2015-09-02 12:43:05', 147),
       (5, '2017-03-30 13:12:55', 50),
       (1, '2018-07-25 14:11:11', 22),
       (2, '2012-09-21 15:05:06', 40),
       (3, '2012-12-21 16:01:01', 1200),
       (1, '2012-01-02 17:43:05', 600),
       (5, '2017-10-30 18:12:55', 800);

select facid, EXTRACT(month FROM starttime) as month, sum(slots)
from cd.bookings
where extract(year from starttime) = 2012
group by month, facid
order by facid, month