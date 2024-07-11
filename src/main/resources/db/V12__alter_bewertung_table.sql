ALTER TABLE wertung
    ADD COLUMN bewerter VARCHAR(255) NOT NULL;

ALTER TABLE wertung
    ADD CONSTRAINT fk_bewerter
        FOREIGN KEY (bewerter)
            REFERENCES bewerter(uuid);