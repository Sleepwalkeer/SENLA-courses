--Produce a count of the number of facilities that have a cost to guests of 10 or more.
CREATE TABLE cd.facilities
(
    facid     integer PRIMARY KEY,
    guestcost numeric NOT NULL

);
insert into cd.facilities(facid, guestcost)
values (0, 150),
       (1, 300),
       (2, 7.5),
       (3, 10),
       (4, 200),
       (5, 15),
       (6, 400),
       (7, 5),
       (8, 2),
       (9, 2);
SELECT count(*)
FROM cd.facilities
WHERE guestcost >= 10;