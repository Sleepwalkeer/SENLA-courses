--Produce a count of the number of recommendations each member has made. Order by member ID.
CREATE TABLE cd.members
(
    memid         integer PRIMARY KEY,
    recommendedby integer
);
insert into cd.members(memid, recommendedby)
values (0, NULL),
       (1, NULL),
       (2, 3),
       (3, 5),
       (4, NULL),
       (5, 0),
       (6, 8),
       (7, 2),
       (8, 1),
       (9, 1),
       (10, 0),
       (11, 8),
       (12, 2),
       (13, 1),
       (14, 1);

SELECT recommendedby, count(*)
FROM cd.members
WHERE recommendedby IS NOT NULL
GROUP BY recommendedby
ORDER BY recommendedby;