--liquibase formatted sql

--changeset tkindy:1-add-players
CREATE TABLE "players" (
  "id" SERIAL PRIMARY KEY,
  "name" VARCHAR(16) NOT NULL
);
