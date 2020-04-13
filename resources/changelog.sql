--liquibase formatted sql

--changeset tkindy:create-users
CREATE TABLE users (
  id INT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id)
);

--changeset tkindy:create-rooms
CREATE TABLE rooms (
  id INT NOT NULL AUTO_INCREMENT,
  passphrase VARCHAR(16) NOT NULL,
  PRIMARY KEY (id)
);

--changeset tkindy:create-roomUsers
CREATE TABLE roomUsers (
  roomId INT NOT NULL,
  userId INT NOT NULL,
  PRIMARY KEY (roomId, userId)
);
