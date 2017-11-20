CREATE TABLE risk_register
(
  id_risk_register           BIGSERIAL     NOT NULL PRIMARY KEY,
  description_risk_register  VARCHAR(60)   UNIQUE NOT NULL
);