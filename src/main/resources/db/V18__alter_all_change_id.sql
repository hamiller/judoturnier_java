ALTER TABLE begegnungen RENAME COLUMN uuid to id;
ALTER TABLE benutzer RENAME COLUMN uuid to id;
ALTER TABLE gewichtsklassengruppen RENAME COLUMN uuid to id;
ALTER TABLE turnier RENAME COLUMN uuid to id;
ALTER TABLE turnier_rollen RENAME COLUMN uuid to id;
ALTER TABLE verein RENAME COLUMN uuid to id;
ALTER TABLE wertung RENAME COLUMN uuid to id;
ALTER TABLE wettkaempfer RENAME COLUMN uuid to id;
ALTER TABLE wettkampfgruppe RENAME COLUMN uuid to id;

ALTER TABLE begegnungen ALTER COLUMN id TYPE UUID using id::uuid;
ALTER TABLE benutzer ALTER COLUMN id TYPE UUID using id::uuid;
ALTER TABLE gewichtsklassengruppen ALTER COLUMN id TYPE UUID using id::uuid;
ALTER TABLE turnier ALTER COLUMN id TYPE UUID using id::uuid;
ALTER TABLE turnier_rollen ALTER COLUMN id TYPE UUID using id::uuid;
ALTER TABLE verein ALTER COLUMN id TYPE UUID using id::uuid;
ALTER TABLE wertung ALTER COLUMN id TYPE UUID using id::uuid;
ALTER TABLE wettkaempfer ALTER COLUMN id TYPE UUID using id::uuid;
ALTER TABLE wettkampfgruppe ALTER COLUMN id TYPE UUID using id::uuid;

ALTER TABLE begegnung_wertung ALTER COLUMN begegnung_id TYPE UUID using begegnung_id::uuid;
ALTER TABLE begegnung_wertung ALTER COLUMN wertung_id TYPE UUID using wertung_id::uuid;
