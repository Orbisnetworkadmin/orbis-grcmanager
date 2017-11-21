CREATE TABLE riskregister
(
 id_risk_register                SERIAL NOT NULL PRIMARY KEY,
 id_risk                         VARCHAR(30) NOT NULL,
 id_risk_subtype                 VARCHAR(30) NOT NULL,
 id_campaign                     VARCHAR(30) NOT NULL,
 status_risk_register            VARCHAR(30) NOT NULL,
 owner_risk_register             VARCHAR(30) NOT NULL,
 description_risk_register       VARCHAR(255) NOT NULL,
 efect_risk_register             VARCHAR(255) NOT NULL,
 location_risk_register          VARCHAR(60) NOT NULL,
 id_treatment                    VARCHAR(30) NOT NULL,
 key_risk_register               BOOLEAN NOT NULL DEFAULT FALSE,
 likelihood_risk_register        DOUBLE PRECISION,
 impact_risk_register            DOUBLE PRECISION,
 inherent_risk_register          DOUBLE PRECISION,
 current_risk_register           DOUBLE PRECISION,
 ecd_risk_register               DOUBLE PRECISION,
 ece_risk_register               DOUBLE PRECISION,
 residual_risk_register          DOUBLE PRECISION,
 startdate_identificacion        VARCHAR(30),
 enddate_identificacion          VARCHAR(30),
 technique_identificacion        VARCHAR(30),
 status_identificacion           VARCHAR(30),
 startdate_analisis              VARCHAR(30),
 enddate_analisis                VARCHAR(30),
 technique_analisis              VARCHAR(30),
 status_analisis                 VARCHAR(30),
 startdate_evaluacion            VARCHAR(30),
 enddate_evaluacion              VARCHAR(30),
 technique_evaluacion            VARCHAR(30),
 status_evaluacion               VARCHAR(30),
 startdate_tratamiento           VARCHAR(30),
 enddate_tratamiento             VARCHAR(30),
 technique_tratamiento           VARCHAR(30),
 status_tratamiento              VARCHAR(30),
 startdate_monitoreo             VARCHAR(30),
 enddate_monitoreo               VARCHAR(30),
 technique_monitoreo             VARCHAR(30),
 status_monitoreo                VARCHAR(30),
 kri_risk_register_title         VARCHAR(30),
 kri_risk_register_descritpion   VARCHAR(255),
 delete_date                     TIME NULL
);
--;; Risk-Register Resumen General
