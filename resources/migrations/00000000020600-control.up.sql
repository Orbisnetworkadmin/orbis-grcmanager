CREATE TABLE control
(
  id_control          BIGSERIAL      NOT NULL PRIMARY KEY,
  id_control_type     BIGINT         NOT NULL REFERENCES control_type (id_control_type),
  description_control VARCHAR(30)   UNIQUE NOT NULL
);
--;;