CREATE TABLE risk_register_fase_technique
(
 id_risk_register_fase_technique  BIGSERIAL      NOT NULL PRIMARY KEY,
 id_technique                     BIGINT UNIQUE NOT NULL REFERENCES technique (id_technique),
 id_fase                          BIGINT UNIQUE NOT NULL REFERENCES fase (id_fase),
 id_risk_register                 BIGINT UNIQUE NOT NULL REFERENCES risk_register (id_risk_register),
 description_rrft                 VARCHAR(255)
);
--;;
