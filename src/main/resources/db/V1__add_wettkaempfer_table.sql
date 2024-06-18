CREATE TABLE wettkaempfer (
                              id SERIAL,
                              "name" VARCHAR(50),
                              geschlecht VARCHAR(1),
                              altersklasse VARCHAR(10),
                              verein BIGINT,
                              gewicht FLOAT,
                              farbe VARCHAR(10),
                              checked BOOLEAN,
                              printed BOOLEAN,
                              PRIMARY KEY (id)
);