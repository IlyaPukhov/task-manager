--liquibase formatted sql

--changeset ilyap:1
ALTER TABLE task
    ADD COLUMN owner_id BIGINT;