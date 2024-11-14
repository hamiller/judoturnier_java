ALTER TABLE bewerter RENAME TO benutzer;

-- general roles of user
ALTER TABLE benutzer ADD COLUMN rollen TEXT;

-- turnier specific roles of user
CREATE TABLE turnier_rollen
(
    benutzer_uuid VARCHAR(255) NOT NULL,
    turnier_uuid VARCHAR(255) NOT NULL,
    rollen TEXT,  -- multiple entries
    PRIMARY KEY (benutzer_uuid, turnier_uuid)
);
