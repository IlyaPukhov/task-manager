--liquibase formatted sql

--changeset ilyap:1
CREATE TABLE IF NOT EXISTS task (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(64)  NOT NULL UNIQUE,
    description VARCHAR(256) NOT NULL,
    status      VARCHAR(32),
    priority    VARCHAR(32)
);