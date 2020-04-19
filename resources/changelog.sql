--liquibase formatted sql

--changeset tkindy:1-add-players
CREATE TABLE "players" (
  "id" SERIAL PRIMARY KEY,
  "name" VARCHAR(16) NOT NULL
);

--changeset tkindy:2-add-rooms
CREATE TABLE "rooms" (
  "id" SERIAL PRIMARY KEY,
  "passphrase" VARCHAR(16) NOT NULL,
  "playlistId" VARCHAR(32) NOT NULL,
  "leaderId" INTEGER NOT NULL REFERENCES "players"("id")
);

--changeset tkindy:3-add-room-players
CREATE TABLE "roomPlayers" (
  "playerId" INTEGER REFERENCES "players"("id") PRIMARY KEY,
  "roomId"  INTEGER NOT NULL REFERENCES "rooms"("id")
);

--changeset tkindy:4-add-room-players-index
CREATE INDEX "roomPlayers_roomId" ON "roomPlayers" ("roomId");
