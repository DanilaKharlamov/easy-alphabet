--liquibase formatted sql
--changeset dkharlamov:1.0.0-02_create_table_language_code labels:easy-alphabet
CREATE TABLE language_code
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY
        CONSTRAINT language_pk PRIMARY KEY,
    code VARCHAR(5) NOT NULL
);
COMMENT ON TABLE language_code is 'Языковой код алфавита страны';
COMMENT ON COLUMN language_code.code is 'аббревиатура';

--rollback DROP TABLE language_code