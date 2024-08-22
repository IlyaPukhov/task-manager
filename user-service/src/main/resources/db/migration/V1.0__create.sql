CREATE TABLE IF NOT EXISTS users (
    id        SERIAL PRIMARY KEY,
    username  VARCHAR(64)  NOT NULL UNIQUE,
    firstname VARCHAR(64)  NOT NULL,
    lastname  VARCHAR(64)  NOT NULL,
    birthdate DATE         NOT NULL,
    email     VARCHAR(256) NOT NULL,
    biography VARCHAR(512)
);