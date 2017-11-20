CREATE TABLE control_procedure
(
  id_risk_register              BIGSERIAL NOT NULL REFERENCES risk_register (id_risk_register),
  id_control                    BIGINT NOT NULL REFERENCES control (id_control),
  description_control_procedure VARCHAR(30) UNIQUE NOT NULL
);
--;;