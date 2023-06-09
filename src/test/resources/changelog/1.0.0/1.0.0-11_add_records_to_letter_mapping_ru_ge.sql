--liquibase formatted sql
--changeset dkharlamov:1.0.0-10_add_records_to_letter_mapping labels:easy-alphabet
INSERT INTO letter_mapping (letter_1, letter_2)
VALUES (1, 100),
       (2, 101),
       (3, 105),
       (4, 102),
       (4, 122),
       (5, 103),
       (6, 104),
       (8, 115),
       (9, 106),
       (10, 108),
       (11, 108),
       (12, 109),
       (12, 121),
       (12, 123),
       (13, 110),
       (14, 111),
       (15, 112),
       (16, 113),
       (17, 114),
       (17, 120),
       (18, 116),
       (19, 117),
       (20, 107),
       (20, 118),
       (21, 119),
       (22, 120),
       (23, 130),
       (23, 132),
       (24, 126),
       (24, 128),
       (25, 125),
       (25, 129),
       (26, 124),
       (27, 124),
       (29, 108),
       (31, 104),
       (32, 133),
       (33, 134),
       (34, 127),
       (35, 131);
--rollback TRUNCATE TABLE letter_mapping
