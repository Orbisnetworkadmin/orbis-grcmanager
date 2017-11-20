CREATE TABLE risk_subtype
(
  id_risk_subtype           BIGSERIAL UNIQUE NOT NULL PRIMARY KEY,
  id_risk_type              BIGINT UNIQUE NOT NULL REFERENCES risk_type (id_risk_type),
  description_risk_subtype  VARCHAR(30)
);
--;;