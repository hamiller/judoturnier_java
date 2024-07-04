CREATE TABLE wettkaempfer
(
    id           SERIAL,
    "name"       VARCHAR(255),
    geschlecht   VARCHAR(255),
    altersklasse VARCHAR(255),
    verein       INTEGER,
    gewicht      FLOAT,
    farbe        VARCHAR(255),
    checked      BOOLEAN,
    printed      BOOLEAN,
    PRIMARY KEY (id)
);