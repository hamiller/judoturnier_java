CREATE TABLE gewichtsklassengruppen
(
    id                SERIAL,
    altersklasse      VARCHAR(255),
    gruppengeschlecht VARCHAR(255),
    maxgewicht        FLOAT,
    mingewicht        FLOAT,
    "name"            VARCHAR(255),
    teilnehmer        INTEGER[],
    PRIMARY KEY (id)
);