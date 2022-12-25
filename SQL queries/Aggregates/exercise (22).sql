--For each day in August 2012, calculate a rolling average of total revenue over the previous 15 days.
-- Output should contain date and revenue columns, sorted by the date.
-- Remember to account for the possibility of a day having zero revenue
CREATE TABLE cd.facilities
(
    facid              integer PRIMARY KEY,
    name               character varying(100) NOT NULL,
    membercost         numeric                NOT NULL,
    guestcost          numeric                NOT NULL,
    initialoutlay      numeric                NOT NULL,
    monthlymaintenance numeric                NOT NULL
);
insert into cd.facilities(facid, name, membercost, guestcost, initialoutlay, monthlymaintenance)
values (0, 'Tennis Court 1', 100, 70, 3000, 1500),
       (1, 'Football field', 200, 140, 6000, 300),
       (2, 'Massage Room 1', 50, 100, 2500, 600),
       (3, 'Tennis Court 2', 600, 350, 4000, 2000),
       (4, 'Tennis Court 3', 400, 300, 8000, 2000),
       (5, 'Table tennis', 200, 350, 5000, 7200),
       (6, 'Football field', 180, 125, 8000, 1400),
       (7, 'Massage Room 2', 800, 1000, 1000, 1200),
       (8, 'Tennis Court 4', 500, 400, 1500, 0),
       (9, 'Tennis Court 5', 600, 500, 1700, 0);

CREATE TABLE cd.bookings
(
    facid     integer NOT NULL,
    memid     integer NOT NULL,
    starttime timestamp PRIMARY KEY,
    slots     integer NOT NULL
);
insert into cd.bookings(facid, memid, starttime, slots)
values (0, 4, '2012-08-21 01:11:11', 50),
       (3, 2, '1999-01-08 02:05:06', 8),
       (4, 1, '2012-08-11 03:01:01', 38),
       (2, 9, '2012-08-03 04:43:05', 14),
       (5, 0, '2017-03-30 05:12:55', 50),
       (1, 3, '2018-07-25 06:11:11', 22),
       (2, 2, '2012-09-21 07:05:06', 40),
       (3, 9, '2012-09-21 08:01:01', 18),
       (1, 0, '2012-08-01 09:43:05', 20),
       (5, 8, '2017-10-30 10:12:55', 25),
       (0, 4, '2012-08-21 11:11:11', 50),
       (3, 2, '1999-01-08 12:05:06', 8),
       (4, 1, '2012-08-11 13:01:01', 38),
       (2, 9, '2012-08-03 14:43:05', 14),
       (5, 0, '2017-03-30 15:12:55', 50),
       (1, 3, '2018-07-25 16:11:11', 22),
       (2, 2, '2012-08-04 17:05:06', 40),
       (3, 9, '2012-08-04 18:01:01', 18),
       (1, 0, '2012-08-02 19:43:05', 20),
       (5, 8, '2017-10-30 20:12:55', 25);
select dategen.date,
       (select sum(case
                       when memid = 0 then slots * facs.guestcost
                       else slots * membercost
           end) as rev

        from cd.bookings bks
                 inner join cd.facilities facs
                            on bks.facid = facs.facid
        where bks.starttime > dategen.date - interval '14 days'
          and bks.starttime < dategen.date + interval '1 day') / 15 as revenue
from (
         -- generates a list of days in august
         select cast(generate_series(timestamp '2012-08-01',
                                     '2012-08-31', '1 day') as date) as date) as dategen
order by dategen.date;