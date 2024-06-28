--liquibase formatted sql

--changeset ilyap:1
CREATE TABLE IF NOT EXISTS task
(
    id          BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title       VARCHAR(64)  NOT NULL UNIQUE,
    description VARCHAR(256) NOT NULL,
    status      VARCHAR(32),
    priority    VARCHAR(32)
);

--changeset ilyap:2
CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128),
    role     VARCHAR(32)
);

--changeset ilyap:3
CREATE TABLE IF NOT EXISTS users_task
(
    id      BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    task_id BIGINT NOT NULL REFERENCES task (id) ON DELETE CASCADE,
    UNIQUE (user_id, task_id)
);
