CREATE TABLE activity
(
id_activity                     BIGSERIAL NOT NULL PRIMARY KEY,
id_dependence_activity          BIGINT,
id_user_control                 BIGINT,
description_activity            VARCHAR(255)    UNIQUE NOT NULL,
startdate_activity              TIMESTAMP       NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
enddate_activity                TIMESTAMP       NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
progress_activity               INTEGER         UNIQUE NOT NULL,
helf_activity                   INTEGER         UNIQUE NOT NULL
);
--;;
