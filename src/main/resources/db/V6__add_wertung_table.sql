CREATE TABLE wertung
(
    uuid                     VARCHAR(255),
    altersklasse             VARCHAR(255),
    strafen_wettkaempfer1    INTEGER,
    punkte_wettkaempfer1     INTEGER,
    strafen_wettkaempfer2    INTEGER,
    punkte_wettkaempfer2     INTEGER,
    sieger_uuid              VARCHAR(255),
    zeit                     BIGINT,
    kampfgeist_wettkaempfer1 INTEGER,
    technik_wettkaempfer1    INTEGER,
    kampfstil_wettkaempfer1  INTEGER,
    fairness_wettkaempfer1   INTEGER,
    kampfgeist_wettkaempfer2 INTEGER,
    technik_wettkaempfer2    INTEGER,
    kampfstil_wettkaempfer2  INTEGER,
    fairness_wettkaempfer2   INTEGER,
    bewerter                 VARCHAR(255) NOT NULL,
    PRIMARY KEY (uuid)
);