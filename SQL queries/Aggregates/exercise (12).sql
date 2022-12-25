--Produce a list of the total number of slots booked per facility per month in the year of 2012.
--In this version, include output rows containing totals for all months per facility,
--and a total for all months for all facilities. The output table should consist of facility id, month and slots,
--sorted by the id and month. When calculating the aggregated values for all months and all facids,
-- return null values in the month and facid columns.
CREATE TABLE cd.bookings
(
    facid     integer NOT NULL,
    starttime timestamp PRIMARY KEY,
    slots     integer NOT NULL
);
insert into cd.bookings(facid, starttime, slots)
values (0, '2012-09-21 01:11:11', 50),
       (3, '2012-01-08 02:05:06', 20),
       (4, '2012-09-21 03:01:01', 2),
       (2, '2015-09-02 04:43:05', 14),
       (5, '2017-03-30 05:12:55', 5),
       (1, '2012-07-25 06:11:11', 22),
       (2, '2012-03-21 07:05:06', 40),
       (3, '2012-02-21 08:01:01', 180),
       (1, '2012-05-02 09:43:05', 200),
       (5, '2017-10-30 10:12:55', 250),
       (0, '2012-06-21 11:11:11', 50),
       (8, '2012-01-08 12:05:06', 20),
       (7, '2012-03-21 13:01:01', 2),
       (6, '2015-09-02 14:43:05', 14),
       (5, '2017-03-30 15:12:55', 5),
       (1, '2018-07-25 16:11:11', 22),
       (2, '2012-10-21 17:05:06', 40),
       (4, '2012-03-21 18:01:01', 180),
       (4, '2012-06-02 19:43:05', 200),
       (4, '2017-10-30 20:12:55', 250);
select facid, extract(month from starttime) as month, sum(slots) as slots
from cd.bookings
where starttime >= '2012-01-01'
  and starttime < '2013-01-01'
group by rollup (facid, month)
order by facid, month;