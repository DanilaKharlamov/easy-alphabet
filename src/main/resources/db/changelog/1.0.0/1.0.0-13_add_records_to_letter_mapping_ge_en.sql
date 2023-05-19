--liquibase formatted sql
--changeset dkharlamov:1.0.0-10_add_records_to_letter_mapping labels:easy-alphabet
INSERT INTO letter_mapping (letter_1, letter_2)
VALUES (100, 200),
       (101, 201),
       (102, 206),
       (103, 203),
       (104, 200),
       (105, 221),
       (105, 222),
       (106, 225),
       (107, 219),
       (108, 204),
       (108, 208),
       (109, 210),
       (110, 211),
       (111, 212),
       (112, 213),
       (113, 124),
       (114, 215),
       (115, 209),
       (115, 227),
       (116, 217),
       (117, 218),
       (118, 219),
       (119, 220),
       (119, 222),
       (120, 215),
       (120, 205),
       (121, 210),
       (122, 206),
       (123, 216),
       (124, 229),
       (125, 228),
       (126, 202),
       (127, 233),
       (128, 202),
       (129, 228),
       (130, 207),
       (130, 232),
       (131, 209),
       (132, 207),
       (135, 223);
--rollback TRUNCATE TABLE letter_mapping
