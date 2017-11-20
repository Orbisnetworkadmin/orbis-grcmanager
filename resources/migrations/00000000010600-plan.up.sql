CREATE TABLE plan
(
  id_plan           BIGSERIAL       NOT NULL PRIMARY KEY,
  description_plan  VARCHAR(255)    UNIQUE NOT NULL,
  startdate_plan    TIMESTAMP       NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  enddate_plan      TIMESTAMP       NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
  progress_plan     INTEGER         UNIQUE NOT NULL
);
--;;
