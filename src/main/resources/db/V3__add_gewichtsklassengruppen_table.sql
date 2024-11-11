CREATE TABLE gewichtsklassengruppen
(
    uuid              VARCHAR(255),
    altersklasse      VARCHAR(255),
    gruppengeschlecht VARCHAR(255),
    maxgewicht        FLOAT,
    mingewicht        FLOAT,
    "name"            VARCHAR(255),
    turnier_uuid      VARCHAR(255),
    PRIMARY KEY (uuid)
);