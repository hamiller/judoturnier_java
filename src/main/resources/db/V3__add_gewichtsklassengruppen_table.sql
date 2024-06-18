CREATE TABLE gewichtsklassengruppen (
                                        id SERIAL,
                                        altersklasse VARCHAR(10),
                                        gruppengeschlecht VARCHAR(1),
                                        maxgewicht FLOAT,
                                        mingewicht FLOAT,
                                        "name" VARCHAR(50),
                                        teilnehmer INTEGER[],
                                        PRIMARY KEY (id)
);