
-- Foreign Key Spalten in turnier_rollen auf UUID ändern
ALTER TABLE turnier_rollen ALTER COLUMN benutzer_uuid TYPE UUID using benutzer_uuid::uuid;
ALTER TABLE turnier_rollen ALTER COLUMN turnier_uuid TYPE UUID using turnier_uuid::uuid;

-- Andere Foreign Key Spalten in verschiedenen Tabellen
ALTER TABLE wettkaempfer ALTER COLUMN verein TYPE UUID using verein::uuid;
ALTER TABLE wettkaempfer ALTER COLUMN turnier_uuid TYPE UUID using turnier_uuid::uuid;

ALTER TABLE wettkampfgruppe ALTER COLUMN turnier_uuid TYPE UUID using turnier_uuid::uuid;

ALTER TABLE begegnungen ALTER COLUMN turnier_uuid TYPE UUID using turnier_uuid::uuid;
ALTER TABLE begegnungen ALTER COLUMN wettkampf_gruppe_id TYPE UUID using wettkampf_gruppe_id::uuid;
ALTER TABLE begegnungen ALTER COLUMN wettkaempfer1 TYPE UUID using wettkaempfer1::uuid;
ALTER TABLE begegnungen ALTER COLUMN wettkaempfer2 TYPE UUID using wettkaempfer2::uuid;

ALTER TABLE wertung ALTER COLUMN bewerter TYPE UUID using bewerter::uuid;
ALTER TABLE wertung ALTER COLUMN sieger_uuid TYPE UUID using sieger_uuid::uuid;

ALTER TABLE begegnung_wertung ALTER COLUMN turnier_uuid TYPE UUID using turnier_uuid::uuid;

ALTER TABLE gewichtsklassengruppen ALTER COLUMN turnier_uuid TYPE UUID using turnier_uuid::uuid;

ALTER TABLE verein ALTER COLUMN turnier_uuid TYPE UUID using turnier_uuid::uuid;

ALTER TABLE begegnung_wertung ALTER COLUMN turnier_uuid TYPE UUID using turnier_uuid::uuid;
ALTER TABLE begegnung_wertung ALTER COLUMN begegnung_id TYPE UUID using begegnung_id ::uuid;
ALTER TABLE begegnung_wertung ALTER COLUMN wertung_id TYPE UUID using wertung_id::uuid;

ALTER TABLE einstellungen ALTER COLUMN turnier_uuid TYPE UUID using turnier_uuid::uuid;

ALTER TABLE gewichtsklassengruppen_wettkaempfer ALTER COLUMN turnier_uuid TYPE UUID using turnier_uuid::uuid;
ALTER TABLE gewichtsklassengruppen_wettkaempfer ALTER COLUMN gewichtsklassengruppe_id TYPE UUID using gewichtsklassengruppe_id::uuid;
ALTER TABLE gewichtsklassengruppen_wettkaempfer ALTER COLUMN wettkaempfer_id TYPE UUID using wettkaempfer_id::uuid;
