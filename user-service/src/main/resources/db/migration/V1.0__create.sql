CREATE TABLE IF NOT EXISTS users
(
    id        BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username  VARCHAR(64)  NOT NULL UNIQUE,
    firstname VARCHAR(64)  NOT NULL,
    lastname  VARCHAR(64)  NOT NULL,
    birthdate DATE         NOT NULL,
    email     VARCHAR(256) NOT NULL,
    status    VARCHAR(64)  NOT NULL CHECK ( status IN ('CREATED', 'VERIFIED') ) DEFAULT 'CREATED',
    biography VARCHAR(512)
);