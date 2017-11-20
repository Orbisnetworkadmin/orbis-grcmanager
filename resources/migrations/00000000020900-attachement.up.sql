CREATE TABLE attachement
(
id_attchement                   BIGSERIAL       NOT NULL PRIMARY KEY,
id_risk_register_fase_technique BIGINT          NOT NULL REFERENCES risk_register_fase_technique (id_risk_register_fase_technique),
url_attachement                 VARCHAR(255)    UNIQUE NOT NULL,
name_attachement                VARCHAR(255)    UNIQUE NOT NULL
);
--;;
