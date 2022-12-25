--Produce a monotonically increasing numbered list of members (including guests), ordered by their date of joining.
--Remember that member IDs are not guaranteed to be sequential.
CREATE TABLE cd.members
(
    memid     integer PRIMARY KEY,
    surname   character varying(100) NOT NULL,
    firstname character varying(100) NOT NULL,
    joindate  timestamp              NOT NULL
);
insert into cd.members(memid, surname, firstname, joindate)
values (0, 'GUEST', 'GUEST', '2011-05-01 20:55:43'),
       (1, 'Jones', 'Douglas', '2000-02-06 08:31:28'),
       (2, 'Rumney', 'Henrietta', '2016-12-20 09:40:12'),
       (3, 'Purview', 'Henry', '2010-05-22 22:56:00'),
       (4, 'Crumpet', 'Erica', '2010-08-19 22:07:08'),
       (5, 'Chavez', 'Carlos', '2022-11-03 18:07:35'),
       (6, 'Montana', 'Tony', '2009-04-11 02:23:25'),
       (7, 'Person', 'Another', '2013-06-17 15:17:31'),
       (8, 'Wood', 'Chuck', '2018-01-16 04:57:35'),
       (9, 'Piper', 'Peter', '2012-03-01 00:32:55');

select row_number() over (order by joindate), firstname, surname
from cd.members
order by joindate