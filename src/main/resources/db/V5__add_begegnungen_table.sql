CREATE TABLE begegnungen
(
    uuid                VARCHAR(255),
    runde_uuid          VARCHAR(255),
    matte_id            INTEGER,
    matten_runde        INTEGER,
    gesamt_runde        INTEGER,
    gruppen_runde       INTEGER,
    wettkaempfer1       VARCHAR(255),
    wettkaempfer2       VARCHAR(255),
    wettkampf_gruppe_id VARCHAR(255),
    runde               INTEGER,
    runden_typ          INTEGER,
    paarung             INTEGER,
    turnier_uuid        VARCHAR(255),
    PRIMARY KEY (uuid)
);