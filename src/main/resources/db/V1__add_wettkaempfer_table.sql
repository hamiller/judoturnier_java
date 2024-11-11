CREATE TABLE wettkaempfer
(
    uuid         VARCHAR(255),
    "name"       VARCHAR(255),
    geschlecht   VARCHAR(255),
    altersklasse VARCHAR(255),
    verein       VARCHAR(255),
    gewicht      FLOAT,
    farbe        VARCHAR(255),
    checked      BOOLEAN,
    printed      BOOLEAN,
    turnier_uuid VARCHAR(255),
    PRIMARY KEY (uuid)
);