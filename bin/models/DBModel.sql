DROP TABLE IF EXISTS Flats CASCADE;
DROP TABLE IF EXISTS Houses CASCADE;
DROP TABLE IF EXISTS Coordinates CASCADE;
DROP TABLE IF EXISTS Users CASCADE;

DROP TYPE IF EXISTS Transport CASCADE;
DROP TYPE IF EXISTS View CASCADE;

CREATE TYPE View AS ENUM (
  'STREET',
  'PARK',
  'BAD',
  'NORMAL',
  'GOOD'
);

CREATE TYPE Transport AS ENUM (
  'FEW',
  'LITTLE',
  'NORMAL',
  'ENOUGH'
);

CREATE TABLE Users (
  id SERIAL PRIMARY KEY,
  user_name TEXT UNIQUE NOT NULL,
  password_hash TEXT NOT NULL
);

CREATE TABLE Coordinates (
  id SERIAL PRIMARY KEY,
  x REAL NOT NULL CHECK (x > -817),
  y DOUBLE PRECISION NOT NULL
);

CREATE TABLE Houses (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  year INTEGER NOT NULL CHECK (year > 0),
  number_of_floors BIGINT NOT NULL CHECK (number_of_floors > 0 AND number_of_floors <= 65),
  number_of_flats_on_floor BIGINT NOT NULL CHECK (number_of_flats_on_floor > 0)
);

CREATE TABLE Flats (
  id SERIAL PRIMARY KEY,
  name TEXT NOT NULL CHECK (char_length(name) > 0),
  coordinates_id INTEGER NOT NULL REFERENCES Coordinates(id),
  creation_date DATE NOT NULL,
  area REAL NOT NULL CHECK (area > 0),
  number_of_rooms BIGINT NOT NULL CHECK (number_of_rooms > 0),
  time_to_metro_by_transport REAL NOT NULL CHECK (time_to_metro_by_transport > 0),
  view View NOT NULL,
  transport Transport NOT NULL,
  house_id INTEGER NOT NULL REFERENCES Houses(id),
  created_by_user INTEGER REFERENCES Users(id)
);
