--How can you output a list of all members, including the individual who recommended them (if any)?
-- Ensure that results are ordered by (surname, firstname).
CREATE TABLE cd.members
(
    memid         integer PRIMARY KEY,
    surname       varchar(100) NOT NULL,
    firstname     varchar(100) NOT NULL,
    recommendedby integer
);
insert into cd.members(memid, surname, firstname, recommendedby)
values (0, 'Sarwin', 'Ramnaresh', NULL),
       (1, 'Jones', 'Douglas', NULL),
       (2, 'Rumney', 'Henrietta', 3),
       (3, 'Purview', 'Henry', 5),
       (4, 'Crumpet', 'Erica', NULL),
       (5, 'Carlos', 'Chavez', 0),
       (6, 'Tony', 'Montana', 8),
       (7, 'Another', 'Person', 2),
       (8, 'Chuck', 'Wood', 1),
       (9, 'Peter', 'Piper', 1);

SELECT members.firstname, members.surname, recommends.firstname, recommends.surname
FROM cd.members members
         LEFT JOIN cd.members recommends
                   ON recommends.memid = members.recommendedby
ORDER BY members.surname, members.firstname