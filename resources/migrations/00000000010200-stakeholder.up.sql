CREATE TABLE stakeholder
(
  id_stakeholder        BIGSERIAL   NOT NULL PRIMARY KEY,
  name_stakeholder      VARCHAR(30) UNIQUE NOT NULL,
  lastname_stakeholder  VARCHAR(30) UNIQUE NOT NULL,
  position_stakeholder  VARCHAR(30) UNIQUE NOT NULL,
  area_stakeholder      VARCHAR(30) UNIQUE NOT NULL,
  tph1_stakeholder      VARCHAR(30) UNIQUE NOT NULL,
  tph2_stakeholder      VARCHAR(30) UNIQUE NOT NULL,
  tph3_stakeholder      VARCHAR(30) UNIQUE NOT NULL,
  addr1_stakeholder     VARCHAR(255) UNIQUE NOT NULL,
  addr2_stakeholder     VARCHAR(255) UNIQUE NOT NULL,
  email1_stakeholder    VARCHAR(60) UNIQUE NOT NULL,
  email2_stakeholder    VARCHAR(60) UNIQUE NOT NULL
);
--;;
-- create a default profile / crea una perfil del sistema por defecto
-- INSERT INTO profile (name_profile)
-- VALUES ('Administrator');
