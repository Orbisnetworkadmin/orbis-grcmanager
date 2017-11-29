(ns orbis-grcmanager.handlers.riskregister
  (:require [orbis-grcmanager.attachments :refer [upload-file!]]
            [re-frame.core :refer [dispatch dispatch-sync reg-event-db reg-event-fx]]
            [orbis-grcmanager.routes :refer [navigate!]]
            [orbis-grcmanager.ajax :refer [ajax-error]]
            [ajax.core :refer [DELETE GET POST PUT]]))

; 1 - Creación de la Base de Datos de eventos de Re-Frame:
; La creación de la base de datos genera una llave dentro de la Base de Datos general
; de Re-frame (db), generandose así una sub-bd para nuestra aplicación. Esta llave
; contiene internamente solo los eventos que manejan el flujo re-frame de
; nuestra aplicación.
;
; db {:otra-bd-funcionalidades-reframe
;     :otra-bd-funcionalidades-reframe
;     :mi-bd-para-funcionalidades-reframe}
; Donde db es la Base de datos general de Re-frame, que contiene un sub-conjunto de
; llaves (sub-bases de datos) asociadas.
;
; La base de datos creada de funcionalidades (:mi-bd-para-funcionalidades-reframe)
; internamente maneja llaves asociadas a los datos o componentes que van a ser manejados
; por los eventos y se controlan mediante subscripciones.
; Esta operación se realiza mediante el siguiente comando general
; (reg-event-db
;     :mi-bd-para-funcionalidades-reframe
;     (fn [db [_ mi-estructura-de-datos]]
;     (assoc db :estructura-de-datos  mi-estructura-de-datos)))
; Nota: Es necesario incluir como requeridos los namespaces individuales
; (handlers/mi-handler.cljs)
; como :require ; en el namespace handlers.cljs:
;(:require mi-aplicacion.handlers.mi-handler)

; Base de datos para varios Risk Registers
(reg-event-db
  :set-risk-registers
  (fn [db [_ risk-registers]]
    (assoc db :risk-registers risk-registers)))

(reg-event-db
  :close-risk-registers
  (fn [db _]
    (dissoc db :risk-registers)))

; Base de datos para el Detalle de Risk Register (Individual)
(reg-event-db
  :set-risk-register
  (fn [db [_ risk-register]]
    (assoc db :risk-register risk-register)))

(reg-event-db
  :close-risk-register
  (fn [db _]
    (dissoc db :risk-register)))


; 2 - Definición de los eventos de Re-Frame:
; Los eventos de re-frame definen las acciones que deben ser ejecutadas en caso de producirse
; dicho evento. Estas acciones, se definen mediante una función adjunta la cual
; generalmente se apoya en la librería Ajax (GET, PUT, POST) y los disparadores de
; re-frame(dispatcers). Un flujo de datos comun pudiera presentar lo siguiente:
; a- Lectura a la API para cargar los datos en nuestra estructura de datos (y por consiguiente en
; nuestra BD re-frame
; b - Dispatch donde se asocian el evento y las estructuras de datos asociadas para
; su ejecución.
;
; db {:mi-bd-para-funcionalidades-reframe: mi-estructura-de-datos}
; Donde db es la Base de datos general de Re-frame, que contiene una bd con las estructuras de datos
; asociadas (generalmente coinciden con las salidas de las API).
;
; Esta operación se realiza mediante el siguiente comando general
;(reg-event-fx
;  :mi-evento-load
;  (fn [_ _]
;    (GET "/api/mi-api-swagger"
;         {:handler       #(do
;                            (dispatch [:mi-bd-para-funcionalidades-reframe (:mi-estructura-de-datos-api %)])
;                            (dispatch [:set-active-page :mi-pagina-activa])
;                            )
;          :error-handler #(ajax-error %)})
;    nil))


; Eventos para varios Risk Registers

(reg-event-fx
  :load-risk-register-sumary
  (fn [_ _]
    (GET "/api/riskregisters"
         {:handler       #(do
                            (dispatch [:set-risk-registers (:Riskregisters %)])

                            )

          :error-handler #(ajax-error %)})
    nil))


; Eventos para el Detalle de Risk Register (Individual)
(reg-event-db
  :load-risk-register-by-id
  (fn [db [_ id-risk-register]]
    (GET (str "/api/riskregister/" id-risk-register)
         {:handler       #(do
                            (dispatch-sync [:set-risk-register (:Riskregister %)])
                            (dispatch [:set-active-page :risk-register-detail]))
          :error-handler #(ajax-error %)})
    (dissoc db :Riskregister)))

(reg-event-fx
  :create-risk-register
  (fn [_ [_ {:keys [ id-risk id-risk-subtype id-campaign status-risk-register owner-risk-register
                    description-risk-register efect-risk-register location-risk-register id-treatment  key-risk-register
                    likelihood-risk-register impact-risk-register inherent-risk-register current-risk-register ecd-risk-register
                    ece-risk-register residual-risk-register startdate-identificacion enddate-identificacion technique-identificacion
                    status-identificacion startdate-analisis enddate-analisis technique-analisis status-analisis startdate-evaluacion
                    enddate-evaluacion technique-evaluacion status-evaluacion startdate-tratamiento enddate-tratamiento technique-tratamiento
                    status-tratamiento startdate-monitoreo enddate-monitoreo technique-monitoreo status-monitoreo kri-risk-register-title kri-risk-register-descritpion] :as rr}]]
    (POST "/api/riskregister"
          {:params        {
                           :id-risk                            id-risk
                           :id-risk-subtype                    id-risk-subtype
                           :id-campaign                        id-campaign
                           :status-risk-register               status-risk-register
                           :owner-risk-register                owner-risk-register
                           :description-risk-register          description-risk-register
                           :efect-risk-register                efect-risk-register
                           :location-risk-register             location-risk-register
                           :id-treatment                       id-treatment
                           :key-risk-register                  key-risk-register
                           :likelihood-risk-register           likelihood-risk-register
                           :impact-risk-register               impact-risk-register
                           :inherent-risk-register             inherent-risk-register
                           :current-risk-register              current-risk-register
                           :ecd-risk-register                  ecd-risk-register
                           :ece-risk-register                  ece-risk-register
                           :residual-risk-register             residual-risk-register
                           :startdate-identificacion           startdate-identificacion
                           :enddate-identificacion             enddate-identificacion
                           :technique-identificacion           technique-identificacion
                           :status-identificacion              status-identificacion
                           :startdate-analisis                 startdate-analisis
                           :enddate-analisis                   enddate-analisis
                           :technique-analisis                 technique-analisis
                           :status-analisis                    status-analisis
                           :startdate-evaluacion               startdate-evaluacion
                           :enddate-evaluacion                 enddate-evaluacion
                           :technique-evaluacion               technique-evaluacion
                           :status-evaluacion                  status-evaluacion
                           :startdate-tratamiento              startdate-tratamiento
                           :enddate-tratamiento                enddate-tratamiento
                           :technique-tratamiento              technique-tratamiento
                           :status-tratamiento                 status-tratamiento
                           :startdate-monitoreo                startdate-monitoreo
                           :enddate-monitoreo                  enddate-monitoreo
                           :technique-monitoreo                technique-monitoreo
                           :status-monitoreo                   status-monitoreo
                           :kri-risk-register-title            kri-risk-register-title
                           :kri-risk-register-descritpion      kri-risk-register-descritpion  }
           :handler       #(do
                             (dispatch-sync [:set-risk-register rr %])
                             (navigate! (str "/riskregister" %)))
           :error-handler #(ajax-error %)})
    nil))

;incompleto
(reg-event-fx
  :save-risk-register
  (fn [_ [_ {:keys [id-risk-register id-risk id-risk-subtype id-campaign status-risk-register owner-risk-register
                    description-risk-register efect-risk-register location-risk-register id-treatment  key-risk-register
                    likelihood-risk-register impact-risk-register inherent-risk-register current-risk-register ecd-risk-register
                    ece-risk-register residual-risk-register startdate-identificacion enddate-identificacion technique-identificacion
                    status-identificacion startdate-analisis enddate-analisis technique-analisis status-analisis startdate-evaluacion
                    enddate-evaluacion technique-evaluacion status-evaluacion startdate-tratamiento enddate-tratamiento technique-tratamiento
                    status-tratamiento startdate-monitoreo enddate-monitoreo technique-monitoreo status-monitoreo kri-risk-register-title kri-risk-register-descritpion] :as rr}]]
    (PUT "/api/riskregister"
          {:params        {:id-risk-register            id-risk-register
                           :id-risk                            id-risk
                           :id-risk-subtype                    id-risk-subtype
                           :id-campaign                        id-campaign
                           :status-risk-register               status-risk-register
                           :owner-risk-register                owner-risk-register
                           :description-risk-register          description-risk-register
                           :efect-risk-register                efect-risk-register
                           :location-risk-register             location-risk-register
                           :id-treatment                       id-treatment
                           :key-risk-register                  key-risk-register
                           :likelihood-risk-register           likelihood-risk-register
                           :impact-risk-register               impact-risk-register
                           :inherent-risk-register             inherent-risk-register
                           :current-risk-register              current-risk-register
                           :ecd-risk-register                  ecd-risk-register
                           :ece-risk-register                  ece-risk-register
                           :residual-risk-register             residual-risk-register
                           :startdate-identificacion           startdate-identificacion
                           :enddate-identificacion             enddate-identificacion
                           :technique-identificacion           technique-identificacion
                           :status-identificacion              status-identificacion
                           :startdate-analisis                 startdate-analisis
                           :enddate-analisis                   enddate-analisis
                           :technique-analisis                 technique-analisis
                           :status-analisis                    status-analisis
                           :startdate-evaluacion               startdate-evaluacion
                           :enddate-evaluacion                 enddate-evaluacion
                           :technique-evaluacion               technique-evaluacion
                           :status-evaluacion                  status-evaluacion
                           :startdate-tratamiento              startdate-tratamiento
                           :enddate-tratamiento                enddate-tratamiento
                           :technique-tratamiento              technique-tratamiento
                           :status-tratamiento                 status-tratamiento
                           :startdate-monitoreo                startdate-monitoreo
                           :enddate-monitoreo                  enddate-monitoreo
                           :technique-monitoreo                technique-monitoreo
                           :status-monitoreo                   status-monitoreo
                           :kri-risk-register-title            kri-risk-register-title
                           :kri-risk-register-descritpion      kri-risk-register-descritpion  }
           :handler       #(do
                             (dispatch-sync [:set-risk-register  rr ])
                             (navigate! (str "/riskregister/" id-risk-register)))
           :error-handler #(ajax-error %)})
    nil))