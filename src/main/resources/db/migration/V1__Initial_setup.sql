CREATE TABLE IF NOT EXISTS measurements
(
    id            BIGINT NOT NULL PRIMARY KEY,
    datastructure VARCHAR(255) NOT NULL,
    operation     VARCHAR(255) NOT NULL,
    first         BIGINT       NOT NULL,
    middle        BIGINT       NOT NULL,
    last          BIGINT       NOT NULL,
    timestamp     TIMESTAMP    NOT NULL
);