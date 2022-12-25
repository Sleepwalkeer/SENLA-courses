--Find the upward recommendation chain for member ID 27: that is, the member who recommended them,
-- and the member who recommended that member, and so on. Return member ID, first name, and surname.
-- Order by descending member id.
CREATE TABLE cd.members
(
    memid         integer PRIMARY KEY,
    surname       character varying(100) NOT NULL,
    firstname     character varying(100) NOT NULL,
    recommendedby integer
);
insert into cd.members(memid, surname, firstname, recommendedby)
values (0, 'GUEST', 'GUEST', NULL),
       (1, 'Jones', 'Douglas', NULL),
       (2, 'Rumney', 'Henrietta', 1),
       (3, 'Purview', 'Henry', 5),
       (4, 'Crumpet', 'Erica', NULL),
       (5, 'Chavez', 'Carlos', 0),
       (6, 'Montana', 'Tony', 8),
       (7, 'Person', 'Another', 2),
       (8, 'Wood', 'Chuck', 7),
       (9, 'Piper', 'Peter', 1),
       (10, 'Stark', 'Rosey', NULL),
       (11, 'Stones', 'Marley', NULL),
       (12, 'Stamps', 'Camilla', 3),
       (13, 'Corben', 'Kira', 5),
       (14, 'Haggard', 'Darya', NULL),
       (15, 'Mallard', 'Hannak', 0),
       (16, 'Copus', 'Clark', 8),
       (17, 'Sirson', 'Audra', 2),
       (18, 'Jackson', 'Emma', 16),
       (19, 'Henderson', 'Chloe', 1),
       (20, 'Schmidt', 'Andrew', NULL),
       (21, 'Pena', 'Hazel', NULL),
       (22, 'Pence', 'Gina', 3),
       (23, 'Trump', 'Fay', 5),
       (24, 'Columbus', 'Melanie', 18),
       (25, 'Cameron', 'Connor', 0),
       (26, 'Blair', 'Bradley', 8),
       (27, 'Mist', 'Ryan', 24),
       (28, 'Tedder', 'Alexandra', 3),
       (29, 'Cooper', 'Alex', NULL);
with recursive recommenders(recommender) as (select recommendedby
                                             from cd.members
                                             where memid = 27
                                             union all
                                             select mems.recommendedby
                                             from recommenders recs
                                                      inner join cd.members mems
                                                                 on mems.memid = recs.recommender)
select recs.recommender, mems.firstname, mems.surname
from recommenders recs
         inner join cd.members mems
                    on recs.recommender = mems.memid
order by memid desc
