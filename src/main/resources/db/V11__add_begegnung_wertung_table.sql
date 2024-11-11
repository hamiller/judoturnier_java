CREATE TABLE begegnung_wertung
(
    begegnung_id VARCHAR(255),
    wertung_id   VARCHAR(255),
    turnier_uuid VARCHAR(255),
    PRIMARY KEY (begegnung_id, wertung_id, turnier_uuid)
);