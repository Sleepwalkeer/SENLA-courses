--How can you output a list of all members, including the individual who recommended them (if any),
--without using any joins? Ensure that there are no duplicates in the list,
--and that each firstname + surname pairing is formatted as a column and ordered.
CREATE TABLE cd.members
(
    memid         integer PRIMARY KEY,
    surname       character varying(100) NOT NULL,
    firstname     character varying(100) NOT NULL,
    recommendedby integer
);
insert into cd.members(memid, surname, firstname, recommendedby)
values (0, 'Sarwin', 'Ramnaresh', NULL),
       (1, 'Jones', 'Douglas', NULL),
       (2, 'Rumney', 'Henrietta', 3),
       (3, 'Purview', 'Henry', 5),
       (4, 'Crumpet', 'Erica', NULL),
       (5, 'Chavez', 'Carlos', 0),
       (6, 'Montana', 'Tony', 8),
       (7, 'Person', 'Another', 2),
       (8, 'Wood', 'Chuck', 1),
       (9, 'Piper', 'Peter', 1);
select distinct concat(members.firstname, ' ', members.surname) as member,
                (select concat(recommends.firstname, ' ', recommends.surname) as recommender
                 from cd.members recommends
                 where recommends.memid = members.recommendedby)
from cd.members members
order by member;