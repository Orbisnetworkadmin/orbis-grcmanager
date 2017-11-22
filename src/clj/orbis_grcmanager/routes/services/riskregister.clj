(ns orbis-grcmanager.routes.services.riskregister
  (:require [orbis-grcmanager.db.core :as db]
            [schema.core :as s]
            [ring.util.http-response :refer :all]
            [orbis-grcmanager.routes.services.common :refer [handler]])
  (:import java.util.Date))


; 1- Definición de las estructuras de datos:
; Estas estructura definen los datos y su tipo, posteriormente se incluyen en un contenedor de respuesta
; que define la estructura de datos que entregará el servicio (Routes/Services.clj) y con la siguiente estructura:
;     :datos (Mapa con Datos/Campos)
;     :error (String)
; (Esto hay que cambiarlo a specs)

; Estructura para un solo registro de Risk Register Mapa {}
(def RiskRegister
  {:id-risk-register                                        s/Int
   :id-risk                                                 s/Str
   :id-risk-subtype                                         s/Str
   :id-campaign                                             s/Str
   :status-risk-register                                    s/Str
   :owner-risk-register                                     s/Str
   :description-risk-register                               s/Str
   :efect-risk-register                                     s/Str
   :location-risk-register                                  s/Str
   :id-treatment                                            s/Str
   :key-risk-register                                       s/Bool
   (s/optional-key :likelihood-risk-register)               s/Num
   (s/optional-key :impact-risk-register)                   s/Num
   (s/optional-key :inherent-risk-register)                 s/Num
   (s/optional-key :current-risk-register)                  s/Num
   (s/optional-key :ecd-risk-register)                      s/Num
   (s/optional-key :ece-risk-register)                      s/Num
   (s/optional-key :residual-risk-register)                 s/Num
   (s/optional-key :startdate-identificacion)               (s/maybe s/Str)
   (s/optional-key :enddate-identificacion)                 (s/maybe s/Str)
   (s/optional-key :technique-identificacion)               (s/maybe s/Str)
   (s/optional-key :status-identificacion)                  (s/maybe s/Str)
   (s/optional-key :startdate-analisis)                     (s/maybe s/Str)
   (s/optional-key :enddate-analisis)                       (s/maybe s/Str)
   (s/optional-key :technique-analisis)                     (s/maybe s/Str)
   (s/optional-key :status-analisis)                        (s/maybe s/Str)
   (s/optional-key :startdate-evaluacion)                   (s/maybe s/Str)
   (s/optional-key :enddate-evaluacion)                     (s/maybe s/Str)
   (s/optional-key :technique-evaluacion)                   (s/maybe s/Str)
   (s/optional-key :status-evaluacion)                      (s/maybe s/Str)
   (s/optional-key :startdate-tratamiento)                  (s/maybe s/Str)
   (s/optional-key :enddate-tratamiento)                    (s/maybe s/Str)
   (s/optional-key :technique-tratamiento)                  (s/maybe s/Str)
   (s/optional-key :status-tratamiento)                     (s/maybe s/Str)
   (s/optional-key :startdate-monitoreo)                    (s/maybe s/Str)
   (s/optional-key :enddate-monitoreo)                      (s/maybe s/Str)
   (s/optional-key :technique-monitoreo)                    (s/maybe s/Str)
   (s/optional-key :status-monitoreo)                       (s/maybe s/Str)
   (s/optional-key :kri-risk-register-title)                (s/maybe s/Str)
   (s/optional-key :kri-risk-register-descritpion)          (s/maybe s/Str)})


; Estructura para un set de registros de Risk Registers Arreglo de Mapas [{}]
(def RiskRegisters
  (select-keys RiskRegister
               [:id-risk-register
                :id-risk
                :id-risk-subtype
                :id-campaign
                :status-risk-register
                :owner-risk-register
                :description-risk-register
                :efect-risk-register
                :location-risk-register
                :id-treatment
                :key-risk-register
                (s/optional-key :likelihood-risk-register)
                (s/optional-key :impact-risk-register)
                (s/optional-key :inherent-risk-register)
                (s/optional-key :current-risk-register)
                (s/optional-key :ecd-risk-register)
                (s/optional-key :ece-risk-register)
                (s/optional-key :residual-risk-register)
                (s/optional-key :startdate-identificacion)
                (s/optional-key :enddate-identificacion)
                (s/optional-key :technique-identificacion)
                (s/optional-key :status-identificacion)
                (s/optional-key :startdate-analisis)
                (s/optional-key :enddate-analisis)
                (s/optional-key :technique-analisis)
                (s/optional-key :status-analisis)
                (s/optional-key :startdate-evaluacion)
                (s/optional-key :enddate-evaluacion)
                (s/optional-key :technique-evaluacion)
                (s/optional-key :status-evaluacion)
                (s/optional-key :startdate-tratamiento)
                (s/optional-key :enddate-tratamiento)
                (s/optional-key :technique-tratamiento)
                (s/optional-key :status-tratamiento)
                (s/optional-key :startdate-monitoreo)
                (s/optional-key :enddate-monitoreo)
                (s/optional-key :technique-monitoreo)
                (s/optional-key :status-monitoreo)
                (s/optional-key :kri-risk-register-title)
                (s/optional-key :kri-risk-register-descritpion) ]))


; 2 - Estructuras contenedoras receptoras del Servicio:
(def RiskRegisterResponse
    {(s/optional-key :Riskregister) RiskRegister
     (s/optional-key :error) s/Str})

(def RRSummaryResults
  {(s/optional-key :Riskregisters) [RiskRegisters]
   (s/optional-key :error) s/Str})

;(handler tags []
;  (ok {:tags (db/ranked-tags)}))
;
;(handler add-tag! [m]
;  (ok {:tag (merge m (db/create-tag<! m))}))
;
;(handler all-issues []
;  (ok {:issues (db/issues {})}))
;
;(handler recent-issues [limit]
;  (ok {:issues (db/recently-viewed-issues {:limit limit})}))
;
;(handler add-issue! [issue]
;  (ok (db/create-issue-with-tags! issue)))
;
;(handler update-issue! [issue]
;  (ok (db/update-issue-with-tags! issue)))
;
;(handler issue [m]
;  (if-let [issue (db/support-issue m)]
;    (ok {:issue issue})
;    (bad-request {:error (str "Issue not found for: " m)})))
;
;(handler issues-by-views [m]
;  (ok {:issues (db/issues-by-views m)}))
;
;(handler issues-by-tag [m]
;  (ok {:issues (db/issues-by-tag m)}))
;
;(handler search-issues [m]
;  (ok {:issues (db/search-issues (update m :query #(str "'" % "'")))}))
;
;(handler delete-issue! [m]
;  (ok (db/dissoc-from-tags-and-delete-issue-and-files! m)))

; 3 - Handlers:
; Arman función handler con una macro de ayuda (Services/common.clj) handler [fn name, arg y body] que incluye el manejo de errores
;en la estructura de datos de respuesta (:error). Algunos handler introducen un mensaje de error personalizado inherente a la consulta.

(handler get-all-riskregisters []
         (ok {:Riskregisters (db/riskregisters {})}))

(handler risk-register-by-id [m]
  (if-let [riskregister (db/riskregister-by-id m)]
    (ok {:Riskregister riskregister})
    (bad-request {:error (str "Riesgo no encontrado : " m)})))

(handler insert-risk-register! [riskregister]
         (ok (db/insertar-risk-register! riskregister)))

(handler delete-risk-register! [riskregister]
         (ok (db/delete-riskregister! riskregister)))

(handler update-risk-register! [riskregister]
 (ok (db/update-riskregister! riskregister)))
