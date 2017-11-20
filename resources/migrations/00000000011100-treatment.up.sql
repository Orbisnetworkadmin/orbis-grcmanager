CREATE TABLE treatment
(
  id_treatment           BIGSERIAL      NOT NULL PRIMARY KEY,
  description_treatment  VARCHAR(30)   UNIQUE NOT NULL
);
--;;
--;;
-- create treatment options by default / crea las opciones de tratamiento por defecto
INSERT INTO treatment (description_treatment) VALUES ('Evitar');
INSERT INTO treatment (description_treatment) VALUES ('Aceptar');
INSERT INTO treatment (description_treatment) VALUES ('Mitigar');
INSERT INTO treatment (description_treatment) VALUES ('Trasladar');