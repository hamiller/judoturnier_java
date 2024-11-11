CREATE TABLE gewichtsklassengruppen_wettkaempfer
(
    gewichtsklassengruppe_id VARCHAR(255),
    wettkaempfer_id          VARCHAR(255),
    turnier_uuid             VARCHAR(255),
    PRIMARY KEY (gewichtsklassengruppe_id, wettkaempfer_id, turnier_uuid)
);