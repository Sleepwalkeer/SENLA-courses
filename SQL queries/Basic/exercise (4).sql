--How can you produce a list of facilities that charge a fee to members,
-- and that fee is less than 1/50th of the monthly maintenance cost?
-- Return the facid, facility name, member cost, and monthly maintenance of the facilities in question.
CREATE TABLE cd.facilities
(
    facid              integer PRIMARY KEY,
    name               character varying(200) NOT NULL,
    membercost         numeric                NOT NULL,
    monthlymaintenance numeric                NOT NULL
);
insert into cd.facilities(facid, name, membercost, monthlymaintenance)
values (0, 'Tennis court 1', 5, 200),
       (1, 'Tennis court 2', 5, 200),
       (2, 'Badminton Court', 0, 50),
       (3, 'Massage Room 1', 0, 10),
       (4, 'Massage Room 2', 35, 3000),
       (5, 'Squash Court', 35, 3000),
       (6, 'Snooker Table', 3.5, 80),
       (7, 'Pool Table', 0, 15),
       (8, 'Table Tennis', 0, 15);
SELECT facid, name, membercost, monthlymaintenance
FROM cd.facilities
WHERE membercost > 0
  AND membercost < (monthlymaintenance / 50)
