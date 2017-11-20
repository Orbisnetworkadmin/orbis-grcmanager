CREATE TABLE objects
(
 id_object           BIGSERIAL      NOT NULL PRIMARY KEY,
 description_object  VARCHAR(255)   UNIQUE NOT NULL
);
--;;
-- create a system account for which the stock entries will be created / crea una cuenta del sistema por defecto
-- INSERT INTO users (screenname, email, admin, is_active, last_login, pass)
-- VALUES ('admin', 'administrador@orbisnetwork.com.ve', TRUE, TRUE, (select now() at time zone 'utc'), 'bcrypt+sha512$86186fc28f83b3e3db78bcf8350a3a57$12$8f215420e68fd7922561167b07354f05d8db6d49e212689e');
