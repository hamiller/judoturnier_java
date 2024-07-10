CREATE TABLE begegnung_wettkampfgruppe
(
    begegnung_id       INTEGER,
    wettkampfgruppe_id INTEGER,
    turnier_uuid       VARCHAR(255),
    PRIMARY KEY (begegnung_id, wettkampfgruppe_id, turnier_uuid)
);