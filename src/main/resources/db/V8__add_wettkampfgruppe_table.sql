CREATE TABLE wettkampfgruppe
(
    uuid         VARCHAR(255),
    name         VARCHAR(255),
    typ          VARCHAR(255),
    altersklasse VARCHAR(255),
    turnier_uuid VARCHAR(255),
    PRIMARY KEY (uuid)
);