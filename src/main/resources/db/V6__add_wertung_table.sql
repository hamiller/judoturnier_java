CREATE TABLE wertung
(
    uuid                     VARCHAR(255),
    altersklasse             VARCHAR(255),
    strafen_wettkaempfer1    INTEGER,
    punkte_wettkaempfer1     INTEGER,
    strafen_wettkaempfer2    INTEGER,
    punkte_wettkaempfer2     INTEGER,
    sieger_id                INTEGER,
    zeit                     BIGINT,
    kampfgeist_wettkaempfer1 INTEGER,
    technik_wettkaempfer1    INTEGER,
    kampfstil_wettkaempfer1  INTEGER,
    fairness_wettkaempfer1   INTEGER,
    kampfgeist_wettkaempfer2 INTEGER,
    technik_wettkaempfer2    INTEGER,
    kampfstil_wettkaempfer2  INTEGER,
    fairness_wettkaempfer2   INTEGER,
    PRIMARY KEY (uuid)
);