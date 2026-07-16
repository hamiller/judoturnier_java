ALTER TABLE wertung ADD COLUMN ippon_wettkaempfer1 INTEGER;
ALTER TABLE wertung ADD COLUMN wazari_wettkaempfer1 INTEGER;
ALTER TABLE wertung ADD COLUMN yuko_wettkaempfer1 INTEGER;
ALTER TABLE wertung ADD COLUMN shido_wettkaempfer1 INTEGER;
ALTER TABLE wertung ADD COLUMN hansoku_make_wettkaempfer1 BOOLEAN;
ALTER TABLE wertung ADD COLUMN ippon_wettkaempfer2 INTEGER;
ALTER TABLE wertung ADD COLUMN wazari_wettkaempfer2 INTEGER;
ALTER TABLE wertung ADD COLUMN yuko_wettkaempfer2 INTEGER;
ALTER TABLE wertung ADD COLUMN shido_wettkaempfer2 INTEGER;
ALTER TABLE wertung ADD COLUMN hansoku_make_wettkaempfer2 BOOLEAN;

UPDATE wertung
SET ippon_wettkaempfer1 = 0,
    wazari_wettkaempfer1 = 0,
    yuko_wettkaempfer1 = COALESCE(punkte_wettkaempfer1, 0),
    shido_wettkaempfer1 = COALESCE(strafen_wettkaempfer1, 0),
    hansoku_make_wettkaempfer1 = COALESCE(strafen_wettkaempfer1, 0) >= 3,
    ippon_wettkaempfer2 = 0,
    wazari_wettkaempfer2 = 0,
    yuko_wettkaempfer2 = COALESCE(punkte_wettkaempfer2, 0),
    shido_wettkaempfer2 = COALESCE(strafen_wettkaempfer2, 0),
    hansoku_make_wettkaempfer2 = COALESCE(strafen_wettkaempfer2, 0) >= 3;

ALTER TABLE wertung DROP COLUMN punkte_wettkaempfer1;
ALTER TABLE wertung DROP COLUMN strafen_wettkaempfer1;
ALTER TABLE wertung DROP COLUMN punkte_wettkaempfer2;
ALTER TABLE wertung DROP COLUMN strafen_wettkaempfer2;
