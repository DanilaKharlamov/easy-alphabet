--liquibase formatted sql
--changeset dkharlamov:1.0.0-03_create_table_letter labels:easy-alphabet
CREATE TABLE letter
(
    id            INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL
        CONSTRAINT letter_pk PRIMARY KEY,
    symbol        VARCHAR(5)                               NOT NULL,
    language_code VARCHAR(5)                               NOT NULL
--     CONSTRAINT language_code_fk FOREIGN KEY (language_code_id) REFERENCES language_code (id)
);
comment on table letter is 'Таблица с буквами и звуками различных алфавитов';
comment on column letter.symbol is 'Символьное обозначение буквы';
comment on column letter.language_code is 'Языковой код алфавита хранимой буквы';

--rollback DROP TABLE letter;