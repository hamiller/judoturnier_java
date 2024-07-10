CREATE TABLE gewichtsklassengruppen_wettkaempfer
(
    gewichtsklassengruppe_id INTEGER,
    wettkaempfer_id          INTEGER,
    turnier_uuid             VARCHAR(255),
    PRIMARY KEY (gewichtsklassengruppe_id, wettkaempfer_id, turnier_uuid)
);