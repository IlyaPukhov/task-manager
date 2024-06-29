--liquibase formatted sql

--changeset ilyap:1
DROP TABLE users;

--changeset ilyap:2
DROP TABLE users_task;