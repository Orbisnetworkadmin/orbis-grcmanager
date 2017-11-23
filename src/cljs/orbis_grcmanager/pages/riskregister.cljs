(ns orbis-grcmanager.pages.riskregister
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




(defn nuevo-registro []
  [:a.btn.btn-sm.btn-success.pull-right
   (href "/create-rr") "Nuevo registro"])

(defn editar-registro []
  [:a.btn.btn-sm.btn-success.pull-right
   (href "/edit-rr") "Editar registro"])

; Fin Funciones / Componentes General------------------------------------------------------------------------------------------------------------------






; ---------------------------------------------------------------------------------------------------------------------
; Risk Register Details Edit-Insert-Update
; Selecciona Keys a ser evaluados
(defn select-rr-keys [rr]
  (let [rr-keys [:id-risk
                 id-risk-subtype
                 :id-risk-register
                 :id-campaing
                 :id-treatment-option
                 :description-risk-register
                 :likelihood-risk-register
                 :impact-risk-register
                 :location-risk-register
                 :xlocation-risk-register
                 :keyrisk-risk-register
                 :ece-risk-register
                 :current-risk-register
                 :ecd-risk-register
                 :residual-risk-register
                 :inherent-risk-register
                 ]]
    ))

; Crea un estandard para los campos de un formulario
(defn field-group [label cursor type placeholder]
  [bs/FormGroup
   [bs/ControlLabel
    {:class "col-lg-2"}
    label]
   [:div.col-lg-10
    [bs/FormControl
     {:type        type
      :value       (or @cursor "")
      :on-change   #(reset! cursor (-> % .-target .-value))
      :placeholder placeholder}]]])

; Verifica que el atomo local no este vacio
(defn rr-empty? [rr]
  (->> rr
       (keep #(-> % second not-empty))
       (empty?)))

; Verifica que el atomo tenga la información actualizada
(defn rr-updated? [original-rr edited-rr]
  (if (:id-risk-register edited-rr)
    (not= (select-rr-keys original-rr)
          (select-rr-keys edited-rr))
    (not (rr-empty? edited-rr))))
; Componentes:
; Botones:
; Salvar y Cancelar
(defn control-buttons [original-rr edited-rr]
  (r/with-let [rr-id      (:id-risk-register @edited-rr)
               errors        (r/atom nil)
               confirm-open? (r/atom false)
               cancel-edit   #(navigate!
                                (if rr-id (str "/riskregister/" rr-id) "/all-rr"))]
              [:div.row>div.col-sm-12
               [confirm-modal
                "Discard changes?"
                confirm-open?
                cancel-edit
                "Discard"]
               [validation-modal errors]
               [:div.btn-toolbar.pull-right
                [bs/Button
                 {:bs-style "warning"
                  :on-click #(if (rr-updated? @original-rr @edited-rr)
                               (reset! confirm-open? true)
                               (cancel-edit))}
                 "Cancel"]
                [bs/Button
                 {:bs-style   "success"
                  :pull-right true
                  :on-click   #(when-not (reset! errors (v/validate-create-rr @edited-rr))
                                 (if issue-id
                                   (dispatch [:save-issue @edited-rr])
                                   (dispatch [:create-issue @edited-rr])))}
                 "Save"]]]))

; Página:

(defn edit-rr-page []


  (r/with-let [ estado1 (r/atom false)
               estado2 (r/atom false)
               estado3 (r/atom false)
               estado4 (r/atom false)
               estado5 (r/atom false)
               original-rr  (subscribe [:riskregister])
               edited-rr   (-> @original-rr
                               (update :description-risk-register #(or % ""))
                               (update :location-risk-register #(or % ""))
                               (update :likelihood-risk-register #(or % ""))
                               (update :impact-risk-register #(or % ""))
                               r/atom)

               descripcion          (r/cursor edited-rr [:description-risk-register])
               ubicacion       (r/cursor edited-rr [:location-risk-register])
               probabilidad        (r/cursor edited-rr [:likelihood-risk-register])
               impacto           (r/cursor edited-rr [:impact-risk-register])


               ]
              [v-box
               :size "auto"
               :gap "10px"
               :height "auto"
               :children
               [[:div.row
                 [:div.col-sm-6
                  [:h3.page-title (if @original-rr "Edicion del registro" "Nuevo registro")]]
                 [:div.col-sm-6
                  [control-buttons original-rr edited-rr]]]
                [bs/FormGroup
                 [bs/ControlLabel "Descripcion del riesgo"]
                 [input-text
                  :model descripcion
                  :width "100%"
                  :class "edit-issue-title"
                  :placeholder "Nombre que describe el riesgo"
                  :on-change #(reset! descripcion %)]]
                [bs/FormGroup
                 [bs/ControlLabel "Ubicacion del riesgo"]
                 [input-text
                  :model ubicacion
                  :width "100%"
                  :placeholder "Nombra la ubicacion del riesgo"
                  :on-change #(reset! ubicacion %)]]
                [field-group
                 "Probabilidad"
                 probabilidad
                 :text "Confirm the password for the user"]
                [:div.col-sm-6
                 [:br][:div.row [:div.col-sm-12 [bs/Button
                                                 {:bs-style "link"
                                                  :on-click #(swap! estado1 not)}
                                                 "Identifiacion"
                                                 ]]]


                 [bs/Collapse {:in @estado1 } [:div.row [:div.panel.panel-default [:h1.risk-title "tetas"]]]  ]
                 [control-buttons original-rr edited-rr]]]]



              )


  )
; Fin Risk Register Detail Edit-Insert-Update--------------------------------------------------------------------------------------------------------





; ---------------------------------------------------------------------------------------------------------------------
; Risk Register Sumary

; Funciones:
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

    {::dt/column-key   [:owner-risk-register]
      ::dt/column-label "Propietario"
      ::dt/sorting      {::dt/enabled? true}}

    {::dt/column-key   [:inherent-risk-register]
       ::dt/column-label "VRI"
       ::dt/sorting      {::dt/enabled? true}}

    {::dt/column-key   [:current-risk-register]
        ::dt/column-label "VRC"
        ::dt/sorting      {::dt/enabled? true}}

    {::dt/column-key   [:residual-risk-register]
         ::dt/column-label "VRR"
         ::dt/sorting      {::dt/enabled? true}}

    {::dt/column-key   [:status-risk-register]
     ::dt/column-label "Status"
     ::dt/sorting      {::dt/enabled? true}}

    {::dt/column-key   [:rating]
     ::dt/column-label "Comportamiento"
     ::dt/sorting      {::dt/enabled? false}
     ::dt/render-fn    rating-formatter}
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

(defn formato-columna-progress [Comportamiento]
  [:a {:href (str "/riskregister/" (get-in risk-register [:id-risk-register]))}
   Comportamiento]
  )

(defn selected-rows-preview []
  [:pre
   [:code
    @(subscribe [::dt/selected-items
                          :RegistrodeRiesgos
                          [:risk-registers]])]])


(defn rating-formatter []
  (if
    get-in risk-register [:id-risk-register])
  [:div.row>div.col-sm-12 [bs/ProgressBar
    {:bs-style "info"
     :max 25
     :min 0
     :now  10}]
  [bs/ProgressBar
   {:bs-style "success"
    :max 25
    :min 0
    :now  15}]
   [bs/ProgressBar
    {:max 25
     :min 0
     :now  15}]
   [bs/ProgressBar
    {:bs-style "warning"
     :max 25
     :min 0
     :now  15}]
   [bs/ProgressBar
    {:bs-style "danger"
     :max 25
     :min 0
     :now  15}]
   ])

     ;(defn rating-formatter [rating]
     ;  (reagent/create-class
     ;    {:component-function
     ;     (fn [rating]
     ;       [:div.ui.star.rating {:data-rating rating}])
     ;
     ;     :component-did-mount
     ;     (fn []
     ;       (.ready (js/$ js/document)
     ;               (fn []
     ;                 (.rating (js/$ ".ui.rating") (js-obj "maxRating" 5)))))}))


; Páginas:
(defn risk-register-sumary-page []
  (r/with-let [atomo-risk-registers-local   (subscribe [:risk-registers])]
              [:div.row
               [:div.col-sm-12 [:div.panel.panel-default [tabla-reframe] [control-paginacion] [selector-por-pagina] [rating-formatter]]]]

              ))


; Fin Risk Register Sumary---------------------------------------------------------------------------------------------------------------------





; ---------------------------------------------------------------------------------------------------------------------
; Risk Register Detail
; Define la función de renderización de la tabla re-frame. Se incluyen:

;Pagina:
(defn risk-register-datail-page []
  (r/with-let [rr     (subscribe [:risk-register])
               estado1 (r/atom false)
               estado2 (r/atom false)
               estado3 (r/atom false)
               estado4 (r/atom false)
               estado5 (r/atom false)
               ]

              [:div.container
               [:div.rounded-panel
               [:div.row
                [:div.col-sm-12 [:h3.risk-title  "Detalle de riesgo"]]]
               [:div.row
                 [:div.col-sm-4
                  [:h4 (:description-risk-register @rr)]
                  [:label "Riesgo: "] [:p (:id-risk @rr)]
                  [:label "Tipo: "] [:p (:id-risk-subtype @rr)]
                  [:label "Localización del riego: "] [:p (:location-risk-register @rr)]


                  [bs/Checkbox
                   {:checked   (boolean (:keyrisk-risk-register @rr))}
                   "Reisgo Clave"] ]
                 [:div.col-sm-4
                  [:br]
                  [:label "Propietario: "][:p (:owner-risk-register @rr)]
                  [:label "Campaña: "][:p (:id-campaign @rr)]
                  [:label "Estatus: "][:h3 (:status-risk-register @rr)]


                  ] [:div.col-sm-4 [:br] [:br] [:span.pull-right [bs/Badge (:id-risk-register @rr)]
                                                   ][:ul
                                                               [:li.risk-detail[:label.risk-small "Valor del riesgo residual: "  (:residual-risk-register @rr)]]
                                                               [:li.risk-detail[:label.risk-small "Valor del riesgo inherente: "  (:inherent-risk-register @rr)]]
                                                               [:li.risk-detail[:label.risk-small "Valor del riego continuo: " (:current-risk-register @rr)]]
                                                               ] [:label "Tratamiento: "][:h3 (:id-treatment @rr)]]
                 ] ]
               [:br] [:div.row [:div.col-sm-12 [:div.btn-toolbar.pull-right  [editar-registro] ]]]
               [:br][:div.row [:div.col-sm-12 [bs/Button
                                               {:bs-style "link"
                                                :on-click #(swap! estado1 not)}
                                               "Identifiacion"
                                               ]]]


               [bs/Collapse {:in @estado1 } [:div.rounded-panel
                                             [:div.row
                                              [:div.col-sm-6
                                               [:label "Técnica : "] [:p (:technique-identificacion @rr)]
                                               [:label "Estatus: "] [:p (:status-identificacion @rr) ]]
                                             [:div.col-sm-6
                                              [:label "Fecha de inicio: "] [:p (:startdate-identificacion @rr)]
                                              [:label "Fecha de finalización: "] [:p (:enddate-identificacion @rr) ]

                                              ]
                                                                ]]]

               [:br][:div.row [:div.col-sm-12 [bs/Button
                                               {:bs-style "link"
                                                :on-click #(swap! estado2 not)}
                                               "Analisis"
                                               ]]]


               [bs/Collapse {:in @estado2 } [:div.rounded-panel
                                             [:div.row
                                              [:div.col-sm-4
                                               [:label "Efecto: "] [:p (:efect-risk-register @rr) ]
                                               [:label "Probabilidad: "] [:p (:likelihood-risk-register @rr)]
                                               [:label "Impacto: "] [:p (:impact-risk-register @rr) ]
                                               ]
                                              [:div.col-sm-4
                                               [:label "Técnica : "] [:p (:technique-analisis @rr)]
                                               [:label "Estatus: "] [:p (:status-analisis @rr) ]
                                               [:label "Localización del riego: "] [:p (:location-risk-register @rr) ]
                                               ] [:div.col-sm-4
                                                      [:label "Fecha de inicio: "] [:p (:startdate-analisis @rr)]
                                                       [:label "Fecha de finalización: "] [:p (:enddate-analisis @rr) ]
                                                                                                ]
                                              ]]
                ]

               [:br][:div.row [:div.col-sm-12 [bs/Button
                                               {:bs-style "link"
                                                :on-click #(swap! estado3 not)}
                                               "Evaluacion"
                                               ]]]


               [bs/Collapse {:in @estado3 } [:div.rounded-panel
                                             [:div.row
                                              [:div.col-sm-4
                                               [:label "Valor del riesgo inherente: "] [:p (:inherent-risk-register @rr) ]
                                               [:label "Eficiencia controles existentes(ECE): "] [:p (:ece-risk-register @rr)]
                                               [:label "Valor del riego continuo: "] [:p (:current-risk-register @rr) ]
                                               ]
                                              [:div.col-sm-4
                                               [:label "Técnica : "] [:p (:technique-evaluacion @rr)]
                                               [:label "Estatus: "] [:p (:status-evaluacion @rr) ]
                                               [bs/Checkbox
                                                {:checked   (boolean (:keyrisk-risk-register @rr))}
                                                "Reisgo Clave"]
                                               ]
                                              [:div.col-sm-4
                                                  [:label "Fecha de inicio: "] [:p (:startdate-evaluacion @rr)]
                                                  [:label "Fecha de finalización: "] [:p (:enddate-evaluacion @rr) ]
                                                  ]
                                              ]]  ]
               [:br][:div.row [:div.col-sm-12 [bs/Button
                                               {:bs-style "link"
                                                :on-click #(swap! estado4 not)}
                                               "Tratamiento"
                                               ]]]


               [bs/Collapse {:in @estado4 } [:div.rounded-panel
                                             [:div.row
                                              [:div.col-sm-4
                                               [:label "Tratamiento: "][:h4 (:id-treatment @rr)]
                                               [:label "Eficiencia controles diseñado(ECD): "] [:p (:ecd-risk-register @rr)]
                                               [:label "Valor del riego residual: "] [:p (:residual-risk-register @rr) ]
                                               ]
                                              [:div.col-sm-4
                                               [:label "Técnica : "] [:p (:technique-tratamiento @rr)]
                                               [:label "Estatus: "] [:p (:status-tratamiento @rr) ]
                                               ]
                                              [:div.col-sm-4
                                               [:label "Fecha de inicio: "] [:p (:startdate-tratamiento @rr)]
                                               [:label "Fecha de finalización: "] [:p (:enddate-tratamiento @rr) ]
                                               ]
                                              ]]  ]

               [:br][:div.row [:div.col-sm-12 [bs/Button
                                               {:bs-style "link"
                                                :on-click #(swap! estado5 not)}
                                               "Monitoreo"
                                               ]]]


               [bs/Collapse {:in @estado5 } [:div.rounded-panel
                                             [:div.row
                                              [:div.col-sm-4
                                               [:label "Key risk indicator : "][:p (:kri-risk-register-title @rr)]
                                               [:label "Descripción: "] [:p (:kri-risk-register-descritpion @rr)]
                                               ]
                                              [:div.col-sm-4
                                               [:label "Técnica : "] [:p (:technique-monitoreo @rr)]
                                               [:label "Estatus: "] [:p (:status-monitoreo @rr) ]
                                               ]
                                              [:div.col-sm-4
                                               [:label "Fecha de inicio: "] [:p (:startdate-monitoreo @rr)]
                                               [:label "Fecha de finalización: "] [:p (:enddate-monitoreo @rr) ]
                                               ]
                                              ]]  ]
               [:div.btn-toolbar.pull-right   [editar-registro] ] ]







              ))

; Fin Risk Register Detail---------------------------------------------------------------------------------------------------------

