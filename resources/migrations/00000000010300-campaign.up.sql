CREATE TABLE campaign
(
  id_campaign                     BIGSERIAL       NOT NULL PRIMARY KEY,
  id_stakeholder                  BIGINT          REFERENCES stakeholder (id_stakeholder),
  description_campaign            VARCHAR(255)    UNIQUE NOT NULL,
  startdate_campaign              TIMESTAMP       NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  enddate_campaign                TIMESTAMP       NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  status_campaign                 VARCHAR(255)    UNIQUE NOT NULL
);
--;;
