--liquibase formatted sql
--changeset dkharlamov:1.0.0-05_create_table_paragraph labels:easy-alphabet
CREATE TABLE paragraph
(
    id              BIGINT GENERATED ALWAYS AS IDENTITY
        CONSTRAINT paragraph_pk PRIMARY KEY,
    text_body       TEXT        NOT NULL,
    native_letter   VARCHAR(10) NOT NULL,
    learning_letter VARCHAR(10) NOT NULL,
    in_order        INTEGER     NOT NULL,
    content_id      BIGINT      NOT NULL,
    CONSTRAINT content_fk FOREIGN KEY (content_id) REFERENCES content (id)
);
comment on table paragraph is 'Абзац переведенного текста';
comment on column paragraph.text_body is 'Текст переведенного абзаца';
comment on column paragraph.native_letter is 'Буква на которую будет измененна';
comment on column paragraph.learning_letter is 'Изменяемая буква в абзаце';
comment on column paragraph.in_order is 'Порядковый номер абзаца в тексте';

--rollback DROP TABLE paragraph