--liquibase formatted sql
--changeset dkharlamov:1.0.0-01_create_table_letter_mapping labels:easy-alphabet
CREATE TABLE letter_mapping
(
    letter_1 SMALLINT NOT NULL,
    letter_2 SMALLINT NOT NULL,
    primary key (letter_1, letter_2)
);
comment on table letter_mapping is 'Сопоставление звуков и букв между алфавитами разных стран';
comment on column letter_mapping.letter_1 is 'Первая буква языка одного алфавита';
comment on column letter_mapping.letter_2 is 'Вторая буква языка другого алфавита';

--rollback DROP TABLE letter_mapping