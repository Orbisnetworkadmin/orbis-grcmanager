CREATE TABLE risk_type
(
  id_risk_type          BIGSERIAL     NOT NULL PRIMARY KEY,
  description_risk_type VARCHAR(30)   UNIQUE NOT NULL
);
--;;