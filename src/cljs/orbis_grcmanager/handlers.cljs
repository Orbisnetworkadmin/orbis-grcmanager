(ns orbis-grcmanager.handlers
  (:require [re-frame.core :refer [dispatch dispatch-sync reg-event-db reg-event-fx]]
            [ajax.core :refer [DELETE GET POST PUT]]
            [orbis-grcmanager.db :as db]
            orbis-grcmanager.handlers.admin
            orbis-grcmanager.handlers.issues
            orbis-grcmanager.handlers.tags))


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


(reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(reg-event-db
  :set-active-page
  (fn [db [_ page]]
    (assoc db :active-page page)))


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

(reg-event-fx
  :run-login-events
  (fn [{:keys [db]} _]
    (doseq [event (:login-events db)]
      (dispatch event))))

(reg-event-db
  :add-login-event
  (fn [db [_ event]]
    (update db :login-events conj event)))

(reg-event-db
  :login
  (fn [db [_ user]]
    (dispatch [:run-login-events])
    (assoc db :user user)))

(reg-event-db
  :logout
  (fn [db _]
    (dissoc db :user)))

(reg-event-db
  :set-error
  (fn [db [_ error]]
    (assoc db :error error)))

(reg-event-db
  :clear-error
  (fn [db _]
    (dissoc db :error)))

(reg-event-db
  :unset-loading
  (fn [db _]
    (dissoc db :loading? :error)))

(reg-event-db
  :set-loading
  (fn [db _]
    (assoc db :loading? true
              :error false)))


