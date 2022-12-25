--Produce a list of the total number of hours booked per facility, remembering that a slot lasts half an hour.
--The output table should consist of the facility id, name, and hours booked, sorted by facility id.
--Try formatting the hours to two decimal places.

CREATE TABLE cd.facilities
(
    facid integer PRIMARY KEY,
    name  character varying(100) NOT NULL

);
insert into cd.facilities(facid, name)
values (0, 'Tennis Court 1'),
       (1, 'Football field'),
       (2, 'Massage Room'),
       (3, 'Tennis Court 2'),
       (4, 'Tennis Court 2'),
       (5, 'Table tennis'),
       (6, 'Football field'),
       (7, 'Massage Room'),
       (8, 'Tennis Court 2'),
       (9, 'Tennis Court 2');

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

select facilities.facid,
       facilities.name,
       trim(to_char(sum(bookings.slots) / 2.0, '99999999D99')) as "Total Hours"

from cd.bookings bookings
         inner join cd.facilities facilities
                    on facilities.facid = bookings.facid
group by facilities.facid, facilities.name
order by facilities.facid;