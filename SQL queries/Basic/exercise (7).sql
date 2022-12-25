--How can you produce a list of facilities, with each labelled as 'cheap' or 'expensive'
-- depending on if their monthly maintenance cost is more than $100?
-- Return the name and monthly maintenance of the facilities in question.
CREATE TABLE cd.facilities
(
    facid              integer PRIMARY KEY,
    name               character varying(200) NOT NULL,
    monthlymaintenance numeric                NOT NULL
);
insert into cd.facilities(facid, name, monthlymaintenance)
values (0, 'Tennis court 1', 200),
       (1, 'Tennis court 2', 200),
       (2, 'Badminton Court', 50),
       (3, 'Massage Room 1', 10),
       (4, 'Massage Room 2', 3000),
       (5, 'Squash Court', 3000),
       (6, 'Snooker Table', 80),
       (7, 'Pool Table', 15),
       (8, 'Table Tennis', 15);
SELECT name,
       CASE
           WHEN monthlymaintenance > 100 THEN 'expensive'
           ELSE 'cheap'
           END AS cost
FROM cd.facilities;