CREATE TABLE wettkampfgruppe
(
    id          SERIAL,
    name        VARCHAR(255),
    typ         VARCHAR(255),
    begegnungen BIGINT,
    PRIMARY KEY (id)
);