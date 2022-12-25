--You want a combined list of all surnames and all facility names.
CREATE TABLE cd.facilities
(
    facid integer PRIMARY KEY,
    name  varchar(100) NOT NULL
);

insert into cd.facilities(facid, name)
values (0, 'Tennis court'),
       (1, 'Football field'),
       (2, 'Hockey field'),
       (3, 'Pool table');

CREATE TABLE cd.members
(
    memid   integer PRIMARY KEY,
    surname varchar(100) NOT NULL
);
insert into cd.members(memid, surname)
values (0, 'Sarwin'),
       (1, 'Jones'),
       (2, 'Kay'),
       (3, 'Purview'),
       (4, 'Crumpet'),
       (5, 'Stotes'),
       (6, 'Schmuck');

SELECT name
FROM cd.facilities
UNION
SELECT surname
FROM cd.members;
