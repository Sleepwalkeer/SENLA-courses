--How can you produce a list of all facilities with the word 'Tennis' in their name?
CREATE TABLE cd.facilities
(
    facid integer PRIMARY KEY,
    name  character varying(200) NOT NULL
);
insert into cd.facilities(facid, name)
values (0, 'Tennis court 1'),
       (1, 'Tennis court 2'),
       (2, 'Badminton Court'),
       (3, 'Massage Room 1'),
       (4, 'Massage Room 2'),
       (5, 'Squash Court'),
       (6, 'Snooker Table'),
       (7, 'Pool Table'),
       (8, 'Table Tennis');
SELECT *
FROM cd.facilities
where name LIKE '%Tennis%'
