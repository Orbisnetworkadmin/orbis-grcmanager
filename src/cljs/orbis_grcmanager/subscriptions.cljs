(ns orbis-grcmanager.subscriptions
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :db-state
  (fn [db _]
    db))

; 3 - Subscripciones de los eventos:
; Para que las estructuras de datos y los componentes asociados a las mismas, puedan
; estar atentos ante cualquier cambio en la base de datos de re-frame ante cualquier evento,
; es necesario registrar las subcripciones haciendo referencia al la clave que representa
; la estructura de datos en nuestra bd de eventos
; (:estructura-de-datos)
; Esto se logra al incluir el siguiente comando dentro de la función query en subscriptions.cljs:
;
; (reg-sub :estructura-de-datos query)



(defn query [db [event-id]]
  (event-id db))

(reg-sub :active-page query)

(reg-sub :loading? query)

(reg-sub :user query)

(reg-sub :tags query)

(reg-sub :selected-tag query)

(reg-sub :issue query)

(reg-sub :issues query)

(reg-sub :error query)

(reg-sub :login-events query)

;;admin
(reg-sub :admin/users query)

