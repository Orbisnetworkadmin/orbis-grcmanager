CREATE TABLE status
(
  id_status           BIGSERIAL      NOT NULL PRIMARY KEY,
  id_fase             BIGINT         NOT NULL REFERENCES fase (id_fase),
  description_status  VARCHAR(30)   UNIQUE NOT NULL
);
--;;