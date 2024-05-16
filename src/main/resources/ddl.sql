DROP TABLE IF EXISTS Events;
DROP TABLE IF EXISTS Client;
DROP TABLE IF EXISTS Person;
DROP TABLE IF EXISTS Description;
DROP TABLE IF EXISTS users;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE Description (
                             id              UUID            PRIMARY KEY DEFAULT uuid_generate_v4(),
                             name            VARCHAR(255)    NOT NULL,
                             description     TEXT            NOT NULL
);

CREATE TABLE Person (
                        id              UUID            PRIMARY KEY DEFAULT uuid_generate_v4(),
                        name            VARCHAR(64)     NOT NULL,
                        address         TEXT            NOT NULL UNIQUE
);

CREATE TABLE Events (
                        id              UUID            PRIMARY KEY DEFAULT uuid_generate_v4(),
                        name            VARCHAR(255)    NOT NULL,
                        description     TEXT            NOT NULL
);

CREATE TABLE Client (
                        id              UUID            PRIMARY KEY DEFAULT uuid_generate_v4(),
                        name            VARCHAR(255)    NOT NULL,
                        description_id  UUID            REFERENCES Description(id)
);

CREATE TABLE users (
                       id              UUID            PRIMARY KEY DEFAULT uuid_generate_v4(),
                       login           VARCHAR(32)     NOT NULL UNIQUE,
                       password        VARCHAR(255)    NOT NULL,
                       role            VARCHAR(20)     CHECK (role IN ('admin', 'moder', 'client')) NOT NULL,
                       name            VARCHAR(64)     NOT NULL
);


