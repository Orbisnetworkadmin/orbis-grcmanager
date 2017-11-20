CREATE TABLE risk_register_stakeholder
(
  id_risk_register                 BIGINT UNIQUE NOT NULL REFERENCES risk_register (id_risk_register),
  id_stakeholder                   BIGINT UNIQUE NOT NULL REFERENCES stakeholder (id_stakeholder),
  relation                         VARCHAR(30)
);
--;;
