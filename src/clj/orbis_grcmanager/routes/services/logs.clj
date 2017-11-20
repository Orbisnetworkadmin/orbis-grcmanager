(ns orbis-grcmanager.routes.services.logs
  (:require [orbis-grcmanager.db.core :as db]
            [schema.core :as s]
            [ring.util.http-response :refer :all]
            [orbis-grcmanager.routes.services.common :refer [handler]])
  (:import java.util.Date))

; Estructuras de Datos:
; Definición de las estructuras de datos con scheme, se incluyen el tipo de datos de respuesta
; con la siguiente estructura:
; :datos (Mapa Estructura de Datos)
; :error (String)
; (Esto hay que cambiarlo a specs)

; Estructura para un solo registro de la tabla log
(def Log
  {:id-log                          s/Num
   :id_user                         s/Num
   :date_log                        Date
   :hostname_log                    s/Str
   :resource_log                    s/Num
   :severity_log                    s/Num
   :message_log                     s/Str})

; Estructura para una colección de varios registros de log, en este caso se generan en un vector []
(def LogSummary
  (select-keys Log
               [:id-log
                :id_user
                :date_log
                :hostname_log
                :resource_log
                :severity_log
                :message_log]))

; Estructura para entrega de la colección de varios registros de log, en este caso se generan en un vector []
(def LogSummaryResults
  {(s/optional-key :logs) [LogSummary]
   (s/optional-key :error)  s/Str})



;(def Issue
;  {:support-issue-id             s/Num
;   :title                        s/Str
;   :summary                      s/Str
;   :detail                       s/Str
;   (s/optional-key :create-date) Date
;   :delete-date                  (s/maybe Date)
;   :update-date                  (s/maybe Date)
;   :last-updated-by              s/Num
;   :last-viewed-date             Date
;   :views                        s/Num
;   :created-by                   s/Num
;   (s/optional-key :files)       [s/Str]
;   (s/optional-key :tags)        [s/Str]
;   (s/optional-key :updated-by)  (s/maybe s/Num)
;   :created-by-screenname        s/Str
;   :updated-by-screenname        (s/maybe s/Str)})
;
;(def IssueSummary
;  (select-keys Issue
;               [:support-issue-id
;                :title
;                :summary
;                (s/optional-key :create-date)
;                (s/optional-key :tags)
;                :update-date
;                :last-viewed-date
;                :views]))
;
;
;(def IssueSummaryResults
;  {(s/optional-key :issues) [IssueSummary]
;   (s/optional-key :error)  s/Str})







;Handlers:
;La siguiente sección se encarga de construir las funciones de ayuda que hacen los llamados a la capa persistente
;o de base de datos, con la ayuda de la macro "handler" definida en routes.services.common que también captura de
; manera cetralizada los errores.

; Handler ejecuta cosulta all-logs devuelve todos los logs
(handler get-all-logs []
  (ok {:logs (db/all-logs {})}))

; Handler elimina todos los registros de la tabla logs e inserta un registro con la última acción realizada de
;eliminación.
(handler delete-all-logs! [mapa]
  (ok (db/delete-all-logs-and-register! mapa)))

; Handler inserta un registro en la tala logs que corresponde a una acción realizada en el sistema.
(handler insert-log! [mapa]
         (ok (db/insert-log<! mapa)))




;(handler issues []
;  (ok {:issues (db/issues {})}))

;(handler issue [m]
;  (if-let [issue (db/support-issue m)]
;    (ok {:issue issue})
;    (bad-request {:error (str "Issue not found for: " m)})))

;(handler delete-issue! [m]
;  (ok (db/dissoc-from-tags-and-delete-issue-and-files! m)))

