--liquibase formatted sql
--changeset dkharlamov:1.0.0-06_add_records_to_language_code.sql labels:easy-alphabet
INSERT INTO language_code (id, code)
VALUES (1, 'ru'),
       (2, 'en'),
       (3, 'ge'),
       (4, 'es'),
       (5, 'de'),
       (6, 'uk'),
       (7, 'be'),
       (8, 'kk'),
       (9, 'fr'),
       (10, 'tr'),
       (11, 'hy'),
       (12, 'zh'),
       (13, 'ko'),
       (14, 'hi');

