CREATE TABLE users
(user_id             SERIAL      NOT NULL PRIMARY KEY,
 screenname          VARCHAR(30) UNIQUE NOT NULL,
 pass                TEXT,
 email               VARCHAR(60),
 admin               BOOLEAN,
 last_login          TIMESTAMP,
 is_active           BOOLEAN,
 create_date         TIMESTAMP   NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
 update_date         TIMESTAMP   NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
 loginsattempts_user INTEGER   DEFAULT 1,
 state_user          VARCHAR(1) DEFAULT 'A',
 question1_user      VARCHAR(255),
 question2_user      VARCHAR(255),
 question3_user      VARCHAR(255),
 answer1_user        TEXT,
 answer2_user        TEXT,
 answer3_user        TEXT
);
--;;
-- create a system account for which the stock entries will be created / crea una cuenta del sistema por defecto
INSERT INTO users (screenname, email, admin, is_active, last_login, pass)
VALUES ('admin', 'administrador@orbisnetwork.com.ve', TRUE, TRUE, (select now() at time zone 'utc'), 'bcrypt+sha512$86186fc28f83b3e3db78bcf8350a3a57$12$8f215420e68fd7922561167b07354f05d8db6d49e212689e');
