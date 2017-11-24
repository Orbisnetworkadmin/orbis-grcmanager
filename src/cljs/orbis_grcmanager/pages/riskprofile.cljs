(ns orbis-grcmanager.pages.riskprofile
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe]]
            [orbis-grcmanager.pages.issues :refer [markdown-preview]]
            [orbis-grcmanager.subscriptions :as subs]
            [orbis-grcmanager.key-events :refer [on-enter]]
            [orbis-grcmanager.bootstrap :as bs]
            [orbis-grcmanager.routes :refer [href navigate!]]
            [reagent-data-table.core :as rdt]
            [orbis-grcmanager.validation :as v]
            [re-frame-datatable.core :as dt]
            [re-frame-datatable.views :as views]
            [orbis-grcmanager.pages.common :refer [validation-modal confirm-modal]]
            [re-com.core
             :refer [box v-box h-split v-split title flex-child-style input-text input-textarea]]))

; Funciones / Componentes General:

(defn nuevo-rr []
  [:a.btn.btn-sm.btn-success.pull-right
   (href "/create-rr") "Nuevo"])

(defn editar-registro []
  [:a.btn.btn-sm.btn-success.pull-right
   (href "/edit-rr") "Editar"])

(defn eliminar-rr-group []
  [:a.btn.btn-sm.btn-danger.pull-right
   (href "/delete-group") "Eliminar"])

; Fin Funciones / Componentes General------------------------------------------------------------------------------------------------------------------



; ---------------------------------------------------------------------------------------------------------------------
; Risk Profile Sumary

; Funciones:

(defn buscar-risk-register []
  (r/with-let [search (r/atom nil)
               buscar #(when-let [value (not-empty @search)]
                            (navigate! (str "/riskprofile" value)))]
              [bs/FormGroup
               [bs/InputGroup
                [bs/FormControl
                 {:type        "text"
                  :class       "input-sm"
                  :placeholder "Introduzca el valor de busqueda"
                  :on-change   #(reset! search (-> % .-target .-value))
                  :on-key-down #(on-enter % buscar)}]
                [bs/InputGroup.Button
                 [:button.btn.btn-sm.btn-default
                  {:on-click buscar}
                  "Buscar"]]]]))

; Grid
; Define la función de renderización de la tabla re-frame. Se incluyen:
;;:NombreDeLaTabla
;[:mi-subscripción-re-frame]
;[{::dt/column-key   [:columna-1-llave-reframe]
;  ::dt/sorting      {::dt/enabled? false}
;  ::dt/column-label "Riesgo"}

(defn tabla-reframe []
  [dt/datatable
   :RegistrodeRiesgos
   [:risk-registers]
   [{::dt/column-key   [:id-risk-register]
     ::dt/sorting      {::dt/enabled? false}
     ::dt/column-label "ID"}

    {::dt/column-key   [:id-risk]
     ::dt/sorting      {::dt/enabled? false}
     ::dt/column-label "Riesgo"}

     {::dt/column-key   [:id-risk-subtype]
     ::dt/column-label "Tipo"}

    {::dt/column-key   [:description-risk-register]
     ::dt/column-label "Descripción"
     ::dt/sorting      {::dt/enabled? false}
     ::dt/render-fn    formato-columna-href}

    {::dt/column-key   [:likelihood-risk-register]
     ::dt/column-label "Probabilidad"
     ::dt/sorting      {::dt/enabled? true}}

    {::dt/column-key   [:impact-risk-register]
     ::dt/column-label "Impacto"
     ::dt/sorting      {::dt/enabled? true}}

    {::dt/column-key   [:inherent-risk-register]
     ::dt/column-label "Inherente"
     ::dt/sorting      {::dt/enabled? true}
     ;::dt/render-fn    (semaforo-formatter)
     }

    {::dt/column-key   [:current-risk-register]
     ::dt/column-label "Corriente"
     ::dt/sorting      {::dt/enabled? true}
     ;::dt/render-fn    (semaforo-formatter)
     }

    {::dt/column-key   [:residual-risk-register]
     ::dt/column-label "Residual"
     ::dt/sorting      {::dt/enabled? true}
     ;::dt/render-fn    semaforo-formatter
     }

    {::dt/column-key   [:status-risk-register]
     ::dt/column-label "Status"
     ::dt/sorting      {::dt/enabled? true}}

    ]
   {::dt/pagination    {::dt/enabled? true
                        ::dt/per-page 10}
    ::dt/table-classes [ "table-striped" "table-bordered" "table-hover" "table"],
    ::dt/selection {::dt/enabled? true}}]
  )


; Se hace la referencia al ns views de la librería, para funciones de visualización paginación y selección de página.
;Risk Register:

(defn control-paginacion []
  [views/default-pagination-controls :RegistrodeRiesgos [:risk-registers]]
  )
(defn selector-por-pagina []
  [views/per-page-selector :RegistrodeRiesgos [:risk-registers]]
  )

(defn formato-columna-href [description risk-register]
  [:a {:href (str "/riskregister/" (get-in risk-register [:id-risk-register]))}
   description]
  )

(defn selected-rows-preview []
  [:pre
   [:code
    @(subscribe [::dt/selected-items
                          :RegistrodeRiesgos
                          [:risk-registers]])]])


; Páginas:
(defn risk-profile-sumary-page []
  (r/with-let [atomo-risk-profile-local   (subscribe [:risk-registers])]
              [:div.container
               [:div.row
                  [:div.col-sm-12
                    [:h2 "Risk Profile"]
                    [buscar-risk-register]]
                  [:div.col-sm-12 [:div.panel.panel-default [tabla-reframe] [control-paginacion] [selector-por-pagina]]
                  [selected-rows-preview]
                   ]]]
              ))

; Fin Risk Register Sumary---------------------------------------------------------------------------------------------------------------------




