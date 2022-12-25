--You want to print out a list of all of the facilities and their cost to members.
-- How would you retrieve a list of only facility names and costs?
CREATE TABLE cd.facilities
(
    facid      integer PRIMARY KEY,
    name       character varying(200) NOT NULL,
    membercost numeric                NOT NULL
);
insert into cd.facilities(facid, name, membercost)
values (0, 'Tennis court 1', 5),
       (1, 'Tennis court 2', 5),
       (2, 'Badminton Court', 0),
       (3, 'Massage Room 1', 0),
       (4, 'Massage Room 2', 35),
       (5, 'Squash Court', 35),
       (6, 'Snooker Table', 3.5),
       (7, 'Pool Table', 0),
       (8, 'Table Tennis', 0);
SELECT name, membercost
FROM cd.facilities;
