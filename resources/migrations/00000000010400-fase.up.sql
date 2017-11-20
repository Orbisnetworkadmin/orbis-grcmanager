CREATE TABLE fase
(
  id_fase           BIGSERIAL      NOT NULL PRIMARY KEY,
  description_fase  VARCHAR(255)   UNIQUE NOT NULL
);
--;;
-- create a fases by default / crea unas fases por defecto
INSERT INTO fase (description_fase) VALUES ('Identificación');
INSERT INTO fase (description_fase) VALUES ('Evaluación');
INSERT INTO fase (description_fase) VALUES ('Análisis');
INSERT INTO fase (description_fase) VALUES ('Tratamiento');
INSERT INTO fase (description_fase) VALUES ('Monitoreo');