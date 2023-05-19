--liquibase formatted sql
--changeset dkharlamov:1.0.0-08_add_add_records_to_letter_ge labels:easy-alphabet
INSERT INTO letter (id, symbol, language_code)
VALUES (100, 'ა', 'ge'),
       (101, 'ბ', 'ge'),
       (102, 'გ', 'ge'),
       (103, 'დ', 'ge'),
       (104, 'ე', 'ge'),
       (105, 'ვ', 'ge'),
       (106, 'ზ', 'ge'),
       (107, 'თ', 'ge'),
       (108, 'ი', 'ge'),
       (109, 'კ', 'ge'),
       (110, 'ლ', 'ge'),
       (111, 'მ', 'ge'),
       (112, 'ნ', 'ge'),
       (113, 'ო', 'ge'),
       (114, 'პ', 'ge'),
       (115, 'ჟ', 'ge'),
       (116, 'რ', 'ge'),
       (117, 'ს', 'ge'),
       (118, 'ტ', 'ge'),
       (119, 'უ', 'ge'),
       (120, 'ფ', 'ge'),
       (121, 'ქ', 'ge'),
       (122, 'ღ', 'ge'),
       (123, 'ყ', 'ge'),
       (124, 'შ', 'ge'),
       (125, 'ჩ', 'ge'),
       (126, 'ც', 'ge'),
       (127, 'ძ', 'ge'),
       (128, 'წ', 'ge'),
       (129, 'ჭ', 'ge'),
       (130, 'ხ', 'ge'),
       (131, 'ჯ', 'ge'),
       (132, 'ჰ', 'ge'),
       (133, 'იუ', 'ge'),
       (134, 'ია', 'ge'),
       (135, 'ეკს', 'ge');
--rollback DELETE FROM letter WHERE id BETWEEN 100 and 199;