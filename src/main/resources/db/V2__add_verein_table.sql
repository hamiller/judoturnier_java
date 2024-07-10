CREATE TABLE verein
(
    id           SERIAL,
    "name"       VARCHAR(255),
    turnier_uuid VARCHAR(255),
    PRIMARY KEY (id, turnier_uuid)
);