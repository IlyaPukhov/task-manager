--liquibase formatted sql

--changeset ilyap:1
ALTER TABLE users
    ADD COLUMN created_at TIMESTAMP;

ALTER TABLE users
    ADD COLUMN modified_at TIMESTAMP;

--changeset ilyap:2
ALTER TABLE task
    ADD COLUMN created_at TIMESTAMP;

ALTER TABLE task
    ADD COLUMN modified_at TIMESTAMP;