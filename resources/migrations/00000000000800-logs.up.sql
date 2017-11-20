CREATE EXTENSION IF NOT EXISTS citext WITH SCHEMA public;
--;;
CREATE TABLE log
(
id_log         SERIAL          NOT NULL PRIMARY KEY,
id_user        BIGINT          NOT NULL REFERENCES users (user_id),
date_log       TIMESTAMP       NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
hostname_log   VARCHAR(255)    UNIQUE NOT NULL,
resource_log   INTEGER         UNIQUE NOT NULL,
severity_log   INTEGER         UNIQUE NOT NULL,
message_log    VARCHAR(255)    UNIQUE NOT NULL
);
--;;
