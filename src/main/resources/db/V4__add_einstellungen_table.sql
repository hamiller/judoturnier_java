CREATE TABLE einstellungen
(
    art          VARCHAR(255),
    wert         VARCHAR(255),
    turnier_uuid VARCHAR(255),
    PRIMARY KEY (art, turnier_uuid)
);
