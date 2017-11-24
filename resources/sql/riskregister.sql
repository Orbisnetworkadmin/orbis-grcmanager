-- :name riskregisters :? :*
-- :doc Devuelve todos los registros del Risk Register
SELECT

  riskregister.id_risk_register,
  riskregister.id_risk,
  riskregister.id_risk_subtype,
  riskregister.id_campaign,
  riskregister.status_risk_register,
  riskregister.owner_risk_register,
  riskregister.description_risk_register,
  riskregister.efect_risk_register,
  riskregister.location_risk_register,
  riskregister.id_treatment,
  riskregister.key_risk_register,
  riskregister.likelihood_risk_register,
  riskregister.impact_risk_register,
  riskregister.inherent_risk_register,
  riskregister.current_risk_register,
  riskregister.ecd_risk_register,
  riskregister.ece_risk_register,
  riskregister.residual_risk_register,
  riskregister.startdate_identificacion,
  riskregister.enddate_identificacion,
  riskregister.technique_identificacion,
  riskregister.status_identificacion,
  riskregister.startdate_analisis,
  riskregister.enddate_analisis,
  riskregister.technique_analisis,
  riskregister.status_analisis,
  riskregister.startdate_evaluacion,
  riskregister.enddate_evaluacion,
  riskregister.technique_evaluacion,
  riskregister.status_evaluacion,
  riskregister.startdate_tratamiento,
  riskregister.enddate_tratamiento,
  riskregister.technique_tratamiento,
  riskregister.status_tratamiento,
  riskregister.startdate_monitoreo,
  riskregister.enddate_monitoreo,
  riskregister.technique_monitoreo,
  riskregister.status_monitoreo,
  riskregister.kri_risk_register_title,
  riskregister.kri_risk_register_descritpion

FROM riskregister
WHERE
  riskregister.delete_date IS NULL;

-- :name riskregister-by-id :? :1
-- :doc Gets riskregiter given the id / Devuelve un Risk Register dado el ID
SELECT
  riskregister.id_risk_register,
  riskregister.id_risk,
  riskregister.id_risk_subtype,
  riskregister.id_campaign,
  riskregister.status_risk_register,
  riskregister.owner_risk_register,
  riskregister.description_risk_register,
  riskregister.efect_risk_register,
  riskregister.location_risk_register,
  riskregister.id_treatment,
  riskregister.key_risk_register,
  riskregister.likelihood_risk_register,
  riskregister.impact_risk_register,
  riskregister.inherent_risk_register,
  riskregister.current_risk_register,
  riskregister.ecd_risk_register,
  riskregister.ece_risk_register,
  riskregister.residual_risk_register,
  riskregister.startdate_identificacion,
  riskregister.enddate_identificacion,
  riskregister.technique_identificacion,
  riskregister.status_identificacion,
  riskregister.startdate_analisis,
  riskregister.enddate_analisis,
  riskregister.technique_analisis,
  riskregister.status_analisis,
  riskregister.startdate_evaluacion,
  riskregister.enddate_evaluacion,
  riskregister.technique_evaluacion,
  riskregister.status_evaluacion,
  riskregister.startdate_tratamiento,
  riskregister.enddate_tratamiento,
  riskregister.technique_tratamiento,
  riskregister.status_tratamiento,
  riskregister.startdate_monitoreo,
  riskregister.enddate_monitoreo,
  riskregister.technique_monitoreo,
  riskregister.status_monitoreo,
  riskregister.kri_risk_register_title,
  riskregister.kri_risk_register_descritpion

FROM riskregister
WHERE
  id_risk_register = :id-risk-register AND
  riskregister.delete_date IS NULL;

-- :name insert-riskregister<! :<! :1
-- :doc Inserta un Risk Register en la tabla riskregister
INSERT INTO riskregister
(
  id_risk,
  id_risk_subtype,
  id_campaign,
  status_risk_register,
  owner_risk_register,
  description_risk_register,
  efect_risk_register,
  location_risk_register,
  id_treatment,
  key_risk_register,
  likelihood_risk_register,
  impact_risk_register,
  inherent_risk_register,
  current_risk_register,
  ecd_risk_register,
  ece_risk_register,
  residual_risk_register,
  startdate_identificacion,
  enddate_identificacion,
  technique_identificacion,
  status_identificacion,
  startdate_analisis,
  enddate_analisis,
  technique_analisis,
  status_analisis,
  startdate_evaluacion,
  enddate_evaluacion,
  technique_evaluacion,
  status_evaluacion,
  startdate_tratamiento,
  enddate_tratamiento,
  technique_tratamiento,
  status_tratamiento,
  startdate_monitoreo,
  enddate_monitoreo,
  technique_monitoreo,
  status_monitoreo,
  kri_risk_register_title,
  kri_risk_register_descritpion
)
VALUES
(
    :id-risk,
    :id-risk-subtype,
    :id-campaign,
    :status-risk-register,
    :owner-risk-register,
    :description-risk-register,
    :efect-risk-register,
    :location-risk-register,
    :id-treatment,
    :key-risk-register,
    :likelihood-risk-register,
    :impact-risk-register,
    :inherent-risk-register,
    :current-risk-register,
    :ecd-risk-register,
    :ece-risk-register,
    :residual-risk-register,
    :startdate-identificacion,
    :enddate-identificacion,
    :technique-identificacion,
    :status-identificacion,
    :startdate-analisis,
    :enddate-analisis,
    :technique-analisis,
    :status-analisis,
    :startdate-evaluacion,
    :enddate-evaluacion,
    :technique-evaluacion,
    :status-evaluacion,
    :startdate-tratamiento,
    :enddate-tratamiento,
    :technique-tratamiento,
    :status-tratamiento,
    :startdate-monitoreo,
    :enddate-monitoreo,
    :technique-monitoreo,
    :status-monitoreo,
    :kri-risk-register-title,
    :kri-risk-register-descritpion
 )RETURNING id_risk_register;


-- :name update-riskregister! :! :n
-- :doc Updates the Risk Register  / Actualiza el Risk Register
UPDATE riskregister

SET
  id_risk                       =     :id-risk,
  id_risk_subtype               =     :id-risk-subtype,
  id_campaign                   =     :id-campaign,
  status_risk_register          =     :status-risk-register,
  owner_risk_register           =     :owner-risk-register,
  description_risk_register     =     :description-risk-register,
  efect_risk_register           =     :efect-risk-register,
  location_risk_register        =     :location-risk-register,
  id_treatment                  =     :id-treatment,
  key_risk_register             =     :key-risk-register,
  likelihood_risk_register      =     :likelihood-risk-register,
  impact_risk_register          =     :impact-risk-register,
  inherent_risk_register        =     :inherent-risk-register,
  current_risk_register         =     :current-risk-register,
  ecd_risk_register             =     :ecd-risk-register,
  ece_risk_register             =     :ece-risk-register,
  residual_risk_register        =     :residual-risk-register,
  startdate_identificacion      =     :startdate-identificacion,
  enddate_identificacion        =     :enddate-identificacion,
  technique_identificacion      =     :technique-identificacion,
  status_identificacion         =     :status-identificacion,
  startdate_analisis            =     :startdate-analisis,
  enddate_analisis              =     :enddate-analisis,
  technique_analisis            =     :technique-analisis,
  status_analisis               =     :status-analisis,
  startdate_evaluacion          =     :startdate-evaluacion,
  enddate_evaluacion            =     :enddate-evaluacion,
  technique_evaluacion          =     :technique-evaluacion,
  status_evaluacion             =     :status-evaluacion,
  startdate_tratamiento         =     :startdate-tratamiento,
  enddate_tratamiento           =     :enddate-tratamiento,
  technique_tratamiento         =     :technique-tratamiento,
  status_tratamiento            =     :status-tratamiento,
  startdate_monitoreo           =     :startdate-monitoreo,
  enddate_monitoreo             =     :enddate-monitoreo,
  technique_monitoreo           =     :technique-monitoreo,
  status_monitoreo              =     :status-monitoreo,
  kri_risk_register_title       =     :kri-risk-register-title,
  kri_risk_register_descritpion =     :kri-risk-register-descritpion
WHERE
  id_risk_register = :id-risk-register;


-- :name delete-riskregister! :! :n
-- :doc deletes a riskregister given an ID / Elimina un Risk Register dado el id
UPDATE riskregister
SET delete_date = now()
WHERE id_risk_register = :id-risk-register;

-- :name delete-riskregister-group! :? :*
-- :doc deletes a set of riskregister given an ID / Elimina un grupo de Risk Registers dado el id
update orbis.riskregister as rr
set delete_date = now()
from
  (
    values :tuple*:set
  ) as val(id_risk_register)
where rr.id_risk_register = val.id_risk_register
RETURNING 0;

