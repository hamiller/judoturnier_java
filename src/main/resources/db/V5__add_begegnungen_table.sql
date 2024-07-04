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
    wertung             UUID,
    PRIMARY KEY (id)
);