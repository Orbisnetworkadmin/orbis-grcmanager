CREATE TABLE risk_status
(
  id_risk_register  BIGINT UNIQUE NOT NULL REFERENCES risk_register (id_risk_register),
  id_status         BIGINT UNIQUE NOT NULL REFERENCES status (id_status),
  user_id           BIGINT UNIQUE NOT NULL REFERENCES users (user_id),
  date_risk_status  TIMESTAMP NOT NULL DEFAULT (now() AT TIME ZONE 'utc')
);
--;;
