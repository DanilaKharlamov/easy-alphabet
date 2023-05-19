--liquibase formatted sql
--changeset dkharlamov:1.0.0-04_create_table_content labels:easy-alphabet
CREATE TABLE content
(
    id             BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL
        CONSTRAINT content_pk PRIMARY KEY,
    created_date   TIMESTAMP DEFAULT now(),
    update_date    TIMESTAMP DEFAULT now(),
    status         VARCHAR(10)                         NOT NULL check (status in ('ACCEPTED', 'REJECTED', 'MODERATION')),
    transform_mode VARCHAR(10)                         NOT NULL check (transform_mode in ('ALPHABET', 'RANDOM')),
    off_set        INTEGER                             NOT NULL,
    subject_text   VARCHAR(500)                        NOT NULL,
    text_language  VARCHAR(5)
);
comment on table content is 'Весь обработанный контент';
comment on column content.created_date is 'Время загрузки контента';
comment on column content.update_date is 'Время последней смены статуса';
comment on column content.status is 'Статус контента';
comment on column content.transform_mode is 'Режим изменения контента';
comment on column content.off_set is 'Диапозон изменяемых символов';
comment on column content.subject_text is 'Первый абзац исходного текста';
comment on column content.text_language is 'Язык исходного текста';

--rollback DROP TABLE content;