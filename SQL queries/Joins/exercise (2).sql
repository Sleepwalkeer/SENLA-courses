--How can you produce a list of the start times for bookings for tennis courts, for the date '2012-09-21'?
-- Return a list of start time and facility name pairings, ordered by the time.
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

CREATE TABLE cd.bookings
(
    facid     integer NOT NULL,
    starttime timestamp PRIMARY KEY
);
insert into cd.bookings(facid, starttime)
values (0, '2012-09-21 01:11:11'),
       (3, '1999-01-08 02:05:06'),
       (4, '2012-09-21 03:01:01'),
       (2, '2015-09-02 04:43:05'),
       (5, '2017-03-30 05:12:55'),
       (1, '2018-07-25 06:11:11'),
       (2, '2012-09-21 07:05:06'),
       (3, '2012-09-21 08:01:01'),
       (4, '2012-06-02 09:43:05'),
       (5, '2017-10-30 10:12:55');

SELECT cd.bookings.starttime, cd.facilities.name
FROM cd.bookings
         INNER JOIN cd.facilities
                    ON cd.bookings.facid = cd.facilities.facid
where cd.facilities.name LIKE 'Tennis Court _'
  AND cd.bookings.starttime BETWEEN '2012-09-21 00:00:00' AND '2012-09-21 23:59:59'
ORDER BY cd.bookings.starttime;