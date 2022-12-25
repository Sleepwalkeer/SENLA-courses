--We want to know how many facilities exist - simply produce a total count.
CREATE TABLE cd.facilities
(
    facid integer PRIMARY KEY,
    name  varchar(100) NOT NULL
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
SELECT count(*)
FROM cd.facilities;
