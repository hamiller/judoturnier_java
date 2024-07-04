CREATE TABLE begegnungen
(
    id              SERIAL,
    matte_id        INTEGER,
    matten_runde    INTEGER,
    gruppen_runde   INTEGER,
    wettkampfgruppe INTEGER,
    wettkaempfer1   INTEGER,
    wettkaempfer2   INTEGER,
    gruppe          INTEGER,
    wertung         UUID,
    PRIMARY KEY (id)
);