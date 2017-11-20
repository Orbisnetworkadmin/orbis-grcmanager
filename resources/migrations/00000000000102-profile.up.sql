CREATE TABLE profile
(
 id_profile           BIGSERIAL      NOT NULL PRIMARY KEY,
 name_profile         VARCHAR(30)    UNIQUE NOT NULL
);
--;;
-- create a default profile / crea una perfil del sistema por defecto
INSERT INTO profile (name_profile)
VALUES ('Administrator');
