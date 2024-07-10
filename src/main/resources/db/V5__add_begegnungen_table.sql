CREATE TABLE begegnungen
(
    id                  SERIAL,
    matte_id            INTEGER,
    matten_runde        INTEGER,
    gruppen_runde       INTEGER,
    wettkampfgruppe     INTEGER,
    wettkaempfer1       INTEGER,
    wettkaempfer2       INTEGER,
    wettkampf_gruppe_id INTEGER,
    wertung             VARCHAR(255),
    turnier_uuid        VARCHAR(255),
    PRIMARY KEY (id, turnier_uuid)
);