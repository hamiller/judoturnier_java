CREATE TABLE begegnungen
(
    id              SERIAL,
    matte_id        INTEGER,
    matten_runde    INTEGER,
    gruppen_runde   INTEGER,
    wettkampfgruppe INTEGER,
    wertung         INTEGER,
    PRIMARY KEY (id)
);