CREATE TABLE technique
(
  id_technique          BIGSERIAL      NOT NULL PRIMARY KEY,
  name_technique        VARCHAR(60)    UNIQUE NOT NULL,
  description_technique VARCHAR(255)   UNIQUE NOT NULL
);
--;;