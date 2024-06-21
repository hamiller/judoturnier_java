CREATE TABLE wettkampf
(
    id            SERIAL,
    matte_id      INTEGER,
    matten_runde  INTEGER,
    gruppen_runde INTEGER,
    gruppe        INTEGER,
    begegnung     INTEGER,
    PRIMARY KEY (id)
);