(ns orbis-grcmanager.pages.riskregister
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch subscribe]]
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
; Risk Register Details Edit-Insert-Update
; Selecciona Keys a ser evaluados
(defn select-rr-keys [rr]
  (let [rr-keys [:id-risk-register
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
                 :likelihood-risk-register
                 :impact-risk-register
                 :inherent-risk-register
                 :current-risk-register
                 :ecd-risk-register
                 :ece-risk-register
                 :residual-risk-register
                 :startdate-identificacion
                 :enddate-identificacion
                 :technique-identificacion
                 :status-identificacion
                 :startdate-analisis
                 :enddate-analisis
                 :technique-analisis
                 :status-analisis
                 :startdate-evaluacion
                 :enddate-evaluacion
                 :technique-evaluacion
                 :status-evaluacion
                 :startdate-tratamiento
                 :enddate-tratamiento
                 :technique-tratamiento
                 :status-tratamiento
                 :startdate-monitoreo
                 :enddate-monitoreo
                 :technique-monitoreo
                 :status-monitoreo
                 :kri-risk-register-title
                 :kri-risk-register-descritpion
                 ]]
    ))

; Crea un estandard para los campos de un formulario
(defn field-group [label cursor type placeholder]
  [bs/FormGroup
   [bs/ControlLabel
    {:class "col-sm-12"}
    label]
   [:div.col-sm-12
    [bs/FormControl
     {:type        type
      :value       (or @cursor "")
      :on-change   #(reset! cursor (-> % .-target .-value))
      :placeholder placeholder}]]])

(defn field-group-numbers [label cursor type placeholder]
      [bs/FormGroup
       [bs/ControlLabel
        {:class "col-sm-12"}
        label]
       [:div.col-sm-12
        [bs/FormControl
         {:type        type
          :value       (or @cursor nill)
          :on-change   #(reset! cursor (js/parseFloat(-> % .-target .-value)) )
          :placeholder placeholder}]]])





(defn field-group-select-estatus [label cursor type cc placeholder]
  [bs/FormGroup
   [bs/ControlLabel
    {:class "col-sm-12"}
    label]
   [:div.col-sm-12
    [bs/FormControl
     {:type        type
      :componentClass cc
      :on-change   #(reset! cursor (-> % .-target .-value))
      :placeholder placeholder}
     [:option (or @cursor "")]
     [:option "Abierto"]
     [:option "En curso"]
     [:option "Cerrado"]] ]])

(defn field-group-select-tecnica [label cursor type cc placeholder]
  [bs/FormGroup
   [bs/ControlLabel
    {:class "col-sm-12"}
    label]
   [:div.col-sm-12
    [bs/FormControl
     {:type        type
      :componentClass cc
      :on-change   #(reset! cursor (-> % .-target .-value))
      :placeholder placeholder}
     [:option (or @cursor "")]
     [:option "A"]
     [:option "B"]
     [:option "C"]] ]])

; Verifica que el atomo local no este vacio
(defn rr-empty? [rr]
  (->> rr
       (keep #(-> % second not-empty))
       (empty?)))

;(prepare-register [rr]
;                  ;aqui no recuerdo como se hack, creo que un reset del atomo con un get-in del campo y un js/parseFloat
;                  ;ve a ver como se hace eso, no recuerdo
;                  (rf/reset! rf #(update-in rf ))
;                  )



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
                                (if rr-id (str "/riskregister/" rr-id) "/riskregister"))]
              [:div.row>div.col-sm-12
               [confirm-modal
                "Deshacer los cambios?"
                confirm-open?
                cancel-edit
                "Descartar"]
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
                  :on-click   #(when-not (reset! errors (v/validate-create @edited-rr))
                                 (prepare-register @edited-rr)
                                 (if rr-id
                                   (dispatch [:save-risk-register @edited-rr])
                                   (dispatch [:create-risk-register @edited-rr])))}
                 "Guardar"]]]))

(defn calcular-vri [probabilidad impacto]
   (if (and (empty? probabilidad) (empty? impacto))
     [:h1.risk-title "Valor no calculado"]
     [:h1.risk-title (* probabilidad impacto)]
     )
  )

; Página:
;incompleto
(defn edit-risk-register-page []
      (r/with-let [ estado1 (r/atom false)
               estado2 (r/atom false)
               estado3 (r/atom false)
               estado4 (r/atom false)
               estado5 (r/atom false)
               original-rr  (subscribe [:risk-register])
               edited-rr   (-> @original-rr
                               (update :id-risk #(or % ""))
                               (update :id-risk-subtype #(or % ""))
                               (update :id-campaign #(or % ""))
                               (update :status-risk-register #(or % ""))
                               (update :owner-risk-register #(or % ""))
                               (update :description-risk-register #(or % ""))
                               (update :efect-risk-register #(or % ""))
                               (update :location-risk-register #(or % ""))
                               (update :id-treatment #(or % ""))
                               (update :key-risk-register #(or % ""))
                               (update :likelihood-risk-register #(or % nill))
                               (update :impact-risk-register #(or % nill))
                               (update :inherent-risk-register #(or % nill))
                               (update :current-risk-register #(or % nill))
                               (update :ecd-risk-register #(or % nill))
                               (update :ece-risk-register #(or % nill))
                               (update :residual-risk-register #(or % ""))
                               (update :startdate-identificacion #(or % ""))
                               (update :enddate-identificacion #(or % ""))
                               (update :technique-identificacion #(or % ""))
                               (update :status-identificacion #(or % ""))
                               (update :startdate-analisis #(or % ""))
                               (update :enddate-analisis #(or % ""))
                               (update :technique-analisis #(or % ""))
                               (update :status-analisis #(or % ""))
                               (update :startdate-evaluacion #(or % ""))
                               (update :enddate-evaluacion #(or % ""))
                               (update :technique-evaluacion #(or % ""))
                               (update :status-evaluacion #(or % ""))
                               (update :startdate-tratamiento #(or % ""))
                               (update :enddate-tratamiento #(or % ""))
                               (update :technique-tratamiento #(or % ""))
                               (update :status-tratamiento #(or % ""))
                               (update :startdate-monitoreo #(or % ""))
                               (update :enddate-monitoreo #(or % ""))
                               (update :technique-monitoreo #(or % ""))
                               (update :status-monitoreo #(or % ""))
                               (update :kri-risk-register-title #(or % ""))
                               (update :kri-risk-register-descritpion #(or % ""))
                               r/atom)

               riesgo       (r/cursor edited-rr [:id-risk])
               tipo        (r/cursor edited-rr [:id-risk-subtype])
               campana           (r/cursor edited-rr [:id-campaign])
               estatus          (r/cursor edited-rr [:status-risk-register])
               propietario       (r/cursor edited-rr [:owner-risk-register])
               descripcion       (r/cursor edited-rr [:description-risk-register])
               efecto           (r/cursor edited-rr [:efect-risk-register])
               ubicacion          (r/cursor edited-rr [:location-risk-register])
               tratamiento       (r/cursor edited-rr [:id-treatment])
               clave        (r/cursor edited-rr [:key-risk-register])
               probabilidad           (r/cursor edited-rr [:likelihood-risk-register])
               impacto           (r/cursor edited-rr [:impact-risk-register])
               iherente       (r/cursor edited-rr [:inherent-risk-register])
               corriente        (r/cursor edited-rr [:current-risk-register])
               ecd           (r/cursor edited-rr [:ecd-risk-register])
               ece          (r/cursor edited-rr [:ece-risk-register])
               residual       (r/cursor edited-rr [:residual-risk-register])
               fechaII        (r/cursor edited-rr [:startdate-identificacion])
               fechaSI           (r/cursor edited-rr [:enddate-identificacion])
               tecnicaI          (r/cursor edited-rr [:technique-identificacion])
               estatusI       (r/cursor edited-rr [:status-identificacion])
               fechaIA        (r/cursor edited-rr [:startdate-analisis])
               fechaSA           (r/cursor edited-rr [:enddate-analisis])
               tecnicaA          (r/cursor edited-rr [:technique-analisis])
               estatusA       (r/cursor edited-rr [:status-analisis])
               fechaIE        (r/cursor edited-rr [:startdate-evaluacion])
               fechaSE           (r/cursor edited-rr [:enddate-evaluacion])
               tecnicaE          (r/cursor edited-rr [:technique-evaluacion])
               estatusE       (r/cursor edited-rr [:status-evaluacion])
               fechaIT        (r/cursor edited-rr [:startdate-tratamiento])
               fechaST           (r/cursor edited-rr [:enddate-tratamiento])
               tecnicaT          (r/cursor edited-rr [:technique-tratamiento])
               estatusT       (r/cursor edited-rr [:status-tratamiento])
               fechaIM        (r/cursor edited-rr [:startdate-monitoreo])
               fechaSM           (r/cursor edited-rr [:enddate-monitoreo])
               tecnicaM          (r/cursor edited-rr [:technique-monitoreo])
               estatusM       (r/cursor edited-rr [:status-monitoreo])
               tituloKri        (r/cursor edited-rr [:kri-risk-register-title])
               descripcionKri           (r/cursor edited-rr [:kri-risk-register-descritpion])


               ]

              (def valor
                (*(:likelihood-risk-register @edited-rr)(:impact-risk-register @edited-rr)))

                  (defn control-buttons [original-rr edited-rr]
                        (r/with-let [rr-id      (:id-risk-register @edited-rr)
                                     errors        (r/atom nil)
                                     confirm-open? (r/atom false)
                                     cancel-edit   #(navigate!
                                                      (if rr-id (str "/riskregister/" rr-id) "/riskregister"))]
                                    [:div.row>div.col-sm-12
                                     [confirm-modal
                                      "Deshacer los cambios?"
                                      confirm-open?
                                      cancel-edit
                                      "Descartar"]
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
                                        :on-click   #(when-not (reset! errors (v/validate-create @edited-rr))
                                                               (if rr-id
                                                                 (dispatch [:save-risk-register @edited-rr])
                                                                 (dispatch [:create-risk-register @edited-rr])))}
                                       "Guardar"]]]))

              [:div.container

               [:div.row
                [:div.col-sm-6
                 [:h3.page-title (if @original-rr "Edicion del registro" "Nuevo registro")]]
                [:div.col-sm-6
                 [control-buttons original-rr edited-rr]]]
               [:div.row
                [:div.col-sm-6
                 [bs/FormGroup
                  [bs/ControlLabel "Descripcion del riesgo:"]
                  [input-text
                   :model descripcion
                   :width "100%"
                   :class "edit-issue-title"
                   :placeholder "Nombre que describe el riesgo"
                   :on-change #(reset! descripcion %)]]
                 [bs/FormGroup
                  [bs/ControlLabel "Riesgo:"]
                  [input-text
                   :model tipo
                   :width "100%"
                   :placeholder "Nombra riesgo padre"
                   :on-change #(reset! tipo %)]]
                 ] [:div.col-sm-6
                    [bs/FormGroup
                     [bs/ControlLabel "Propietario del riesgo:"]
                     [input-text
                      :model propietario
                      :width "100%"
                      :class "edit-issue-title"
                      :placeholder "Propietario asociado al riesgo"
                      :on-change #(reset! propietario %)]]
                    [bs/FormGroup
                     [bs/ControlLabel "Campaña:"]
                     [input-text
                      :model campana
                      :width "100%"
                      :placeholder "Nombra la Campaña"
                      :on-change #(reset! campana %)]]
                    [bs/FormGroup
                     [bs/ControlLabel "Estatus del riesgo:"]
                     [input-text
                      :model estatus
                      :width "100%"
                      :placeholder "Nombra el estatus del riesgo"
                      :on-change #(reset! estatus %)]]
                    ]
                ]



                          [:br][:div.row [:div.col-sm-12 [bs/Button
                                                          {:bs-style "link"
                                                           :on-click #(swap! estado1 not)}
                                                          "Identifiacion"
                                                           ]]]


                          [bs/Collapse {:in @estado1 }
                           [:div.row
                            [:div.col-sm-6
                             [field-group-select-estatus
                              "Estatus de la fase identificación"
                              estatusI
                              :text
                              :select "Confirm the password for the user"]
                             [field-group-select-tecnica
                              "Tecnica para la identificación"
                              tecnicaI
                              :text
                              :select "Seleciona la tecnica"]
                             ]
                            [:div.col-sm-6
                             [bs/FormGroup
                              [:label "Fecha de inicio de la identificación"]
                              [:p (:startdate-identificacion @original-rr)]
                              [bs/DateTimeField {:input-format "dddd, MMMM D YYYY, h:mm:ss a" :defaultText "" :onChange #(reset! fechaII  %)}]
                              [:label "Fecha de Finalización de la identificación"]
                              [:p (:enddate-identificacion @original-rr)]
                              [bs/DateTimeField {:input-format "dddd, MMMM D YYYY, h:mm:ss a" :defaultText "" :onChange #(reset! fechaSI %)}]
                               ]
                             [control-buttons original-rr edited-rr]]
                            ]
                           ]


               [:br][:div.row [:div.col-sm-12 [bs/Button
                                               {:bs-style "link"
                                                :on-click #(swap! estado2 not)}
                                               "Análisis"
                                               ]]]


               [bs/Collapse {:in @estado2 } [:div.row
                                             [:div.col-sm-12 [bs/FormGroup
                                                              [bs/ControlLabel "Efecto:"]
                                                              [input-text
                                                               :model efecto
                                                               :width "100%"
                                                               :class "edit-issue-title"
                                                               :placeholder "Nombre que describe el riesgo"
                                                               :on-change #(reset! efecto %)]]]
                                             [:div.col-sm-6
                                              [field-group-select-estatus
                                               "Estatus de la fase de analisis"
                                               estatusA
                                               :text
                                               :select "Selecciona el estatus"]
                                              [field-group-select-tecnica
                                               "Tecnica para la analisis"
                                               tecnicaA
                                               :text
                                               :select "Seleciona la tecnica de evalua"]

                                              [field-group-numbers
                                               "Probabilidad del Riesgo"
                                               probabilidad
                                               :number "Introduzca el valor del la probabilidad"]
                                              [field-group-numbers
                                               "Valor del impacto del Riesgo"
                                               impacto
                                               :number "Introduzca el valor del impacto"]
                                              ]
                                             [:div.col-sm-6
                                              [bs/FormGroup
                                               [bs/ControlLabel "Ubicación:"]
                                               [input-text
                                                :model ubicacion
                                                :width "100%"
                                                :class "edit-issue-title"
                                                :placeholder "Describa la ubicacion del riesgo"
                                                :on-change #(reset! ubicacion %)]]
                                              [bs/FormGroup
                                               [:label "Fecha de inicio de la evaluación"]
                                               [:p (:startdate-analisis @original-rr)]
                                               [bs/DateTimeField {:input-format "dddd, MMMM D YYYY, h:mm:ss a" :defaultText "" :onChange #(reset! fechaIA  %)}]
                                               [:label "Fecha de Finalización de la evaluación"]
                                               [:p (:enddate-analisis @original-rr)]
                                               [bs/DateTimeField {:input-format "dddd, MMMM D YYYY, h:mm:ss a" :defaultText "" :onChange #(reset! fechaSA %)}]
                                               ] [control-buttons original-rr edited-rr]]

                                              ]]


               [:br][:div.row [:div.col-sm-12 [bs/Button
                                               {:bs-style "link"
                                                :on-click #(swap! estado3 not)}
                                               "Evaluación"
                                               ]]]


               [bs/Collapse {:in @estado3 } [:div.row
                                             [:div.col-sm-6
                                              [bs/FormGroup [field-group-select-estatus
                                                             "Estatus de la fase de evaluación"
                                                             estatusE
                                                             :text
                                                             :select "Selecciona el estatus"]
                                               [field-group-select-tecnica
                                                "Tecnica para la evaluación"
                                                tecnicaE
                                                :text
                                                :select "Seleciona la tecnica"]
                                               [field-group-numbers
                                                "Valor del riesgo continuo"
                                                corriente
                                                :number "Introduzca el valor del riesgo continuo"]
                                               [bs/FormGroup
                                                [bs/ControlLabel
                                                 {:class "col-sm-12"}
                                                 "Riesgo inherente"]
                                                [:div.col-sm-12
                                                 [bs/FormControl
                                                  {:type   :number
                                                   :value valor
                                                   :model iherente
                                                   :change-on-blur? false
                                                   :on-changes #(swap! edited-rr assoc :inherent-risk-register valor)
                                                   :placeholder ""}]
                                                 ]]


                                               ]
                                              ]
                                             [:div.col-sm-6
                                              [bs/FormGroup
                                               [field-group-numbers
                                                "Eficiencia controles (ECE)"
                                                ece
                                                :number "Introduzca el valor del ECE"]
                                               [:label "Fecha de inicio de la evaluación"]
                                               [:p (:startdate-evaluacion @original-rr)]
                                               [bs/DateTimeField {:input-format "dddd, MMMM D YYYY, h:mm:ss a" :defaultText "" :onChange #(reset! fechaIE  %)}]
                                               [:label "Fecha de Finalización de la evaluación"]
                                               [:p (:enddate-evaluacion @original-rr)]
                                               [bs/DateTimeField {:input-format "dddd, MMMM D YYYY, h:mm:ss a" :defaultText "" :onChange #(reset! fechaSE %)}]
                                               [bs/Checkbox
                                                {:checked   (boolean (:key-risk-register @edited-rr))
                                                 :on-change #(swap! edited-rr update :key-risk-register not)}
                                                "Riesgo clave"] [control-buttons original-rr edited-rr]] ]
                                             ]  ]


               [:br][:div.row [:div.col-sm-12 [bs/Button
                                               {:bs-style "link"
                                                :on-click #(swap! estado4 not)}
                                               "Tratamiento"
                                               ]]]


               [bs/Collapse {:in @estado4 } [:div.row
                                             [:div.col-sm-6
                                              [bs/FormGroup [field-group-select-estatus
                                                             "Estatus de la fase de tratamiento"
                                                             estatusT
                                                             :text
                                                             :select "Selecciona el estatus"]
                                               [field-group-select-tecnica
                                                "Tecnica para la el tratamiento"
                                                tecnicaT
                                                :text
                                                :select "Seleciona la tecnica a utilizar"]
                                               [field-group
                                                "Eficiencia controles diseñado(ECD)"
                                                ecd
                                                :text "Introduzca el valor del ECD"]
                                               [field-group
                                                "Riesgo residual"
                                                residual
                                                :text "Introduzca el valor del riesgo residual"]
                                               ]
                                              ]
                                             [:div.col-sm-6
                                              [bs/FormGroup
                                               [bs/FormGroup
                                                [bs/ControlLabel "Tratamiento:"]
                                                [input-text
                                                 :model tratamiento
                                                 :width "100%"
                                                 :class "edit-issue-title"
                                                 :placeholder "Nombre que describe el riesgo"
                                                 :on-change #(reset! tratamiento %)]]
                                               [:label "Fecha de inicio de la evaluación"]
                                               [:p (:startdate-tratamiento @original-rr)]
                                               [bs/DateTimeField {:input-format "dddd, MMMM D YYYY, h:mm:ss a" :defaultText "" :onChange #(reset! fechaIT  %)}]
                                               [:label "Fecha de Finalización de la evaluación"]
                                               [:p (:enddate-tratamiento @original-rr)]
                                               [bs/DateTimeField {:input-format "dddd, MMMM D YYYY, h:mm:ss a" :defaultText ""  :onChange #(reset! fechaST %)
                                                                  }]
                                                ] [control-buttons original-rr edited-rr] ]
                                             ]  ]


               [:br][:div.row [:div.col-sm-12 [bs/Button
                                               {:bs-style "link"
                                                :on-click #(swap! estado5 not)}
                                               "Monitoreo"
                                               ]]]


               [bs/Collapse {:in @estado5 } [:div.row
                                             [:div.col-sm-6 [bs/FormGroup
                                                             [bs/ControlLabel "Titulo del KRI:"]
                                                             [input-text
                                                              :model tituloKri
                                                              :width "100%"
                                                              :class "edit-issue-title"
                                                              :placeholder "Nombre que describe al KRI"
                                                              :on-change #(reset! tituloKri %)]]]
                                             [:div.col-sm-12 [bs/FormGroup
                                                              [bs/ControlLabel "Descripción del KRI:"]
                                                              [input-text
                                                               :model descripcionKri
                                                               :width "100%"
                                                               :class "edit-issue-title"
                                                               :placeholder "Nombre que describe al KRI"
                                                               :on-change #(reset! descripcionKri %)]]]
                                             [:div.col-sm-6

                                              [bs/FormGroup [field-group-select-estatus
                                                             "Estatus de la fase de monitoreo"
                                                             estatusM
                                                             :text
                                                             :select "Selecciona el estatus"]
                                               [field-group-select-tecnica
                                                "Tecnica para el monitoreo"
                                                tecnicaM
                                                :text
                                                :select "Seleciona la tecnica de evalua"]
                                               ]
                                              ]
                                             [:div.col-sm-6
                                              [bs/FormGroup
                                               [:label "Fecha de inicio de la evaluación"]
                                               [:p (:startdate-monitoreo @original-rr)]
                                               [bs/DateTimeField {:input-format "dddd, MMMM D YYYY, h:mm:ss a" :defaultText "" :onChange #(reset! fechaIM  %)}]
                                               [:label "Fecha de Finalización de la evaluación"]
                                               [:p (:enddate-monitoreo @original-rr)]
                                               [bs/DateTimeField {:input-format "dddd, MMMM D YYYY, h:mm:ss a" :defaultText "" :onChange #(reset! fechaSM %)}]
                                                ] [control-buttons original-rr edited-rr]]
                                             ]  ]
               ]

              ))
; Fin Risk Register Detail Edit-Insert-Update--------------------------------------------------------------------------------------------------------





; ---------------------------------------------------------------------------------------------------------------------
; Risk Register Sumary

; Funciones:

(defn buscar-risk-register []
  (r/with-let [search (r/atom nil)
               buscar #(when-let [value (not-empty @search)]
                            (navigate! (str "/riskregister" value)))]
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

(defn selected-rows-preview []
  [:pre
   [:code
    @(subscribe [::dt/selected-items
                          :RegistrodeRiesgos
                          [:risk-registers]])]])

(defn rating-formatter [_ risk-registers]
  [:div.row>div.col-sm-12
   [bs/ProgressBar
    (load-progress-bar :inherent-risk-register "VRI"  risk-registers)]
   [bs/ProgressBar
    (load-progress-bar :current-risk-register "VRC" risk-registers)]
   [bs/ProgressBar
    (load-progress-bar :residual-risk-register "VRR" risk-registers)]
   ])

(defn load-progress-bar [clave label risk-registers]
  (if (not (= (get-in risk-registers [clave]) nil))
     (cond
       (and (> (get-in risk-registers [clave]) 3.99) (< (get-in risk-registers [clave]) 7.00))
         {:bs-style "success"
          :max 25
          :min 0
          :now  (get-in risk-registers [clave])
          :label label}
       (and (> (get-in risk-registers [clave]) 6.99) (< (get-in risk-registers [clave]) 10.00))
         {:bs-style "info"
          :max 25
          :min 0
          :now  (get-in risk-registers [clave])
          :label label}
       (and (> (get-in risk-registers [clave]) 9.99) (< (get-in risk-registers [clave]) 13.00))
        {:bs-style "warning"
         :max 25
         :min 0
         :now  (get-in risk-registers [clave])
         :label label}
       (> (get-in risk-registers [clave]) 12.99)
         {:bs-style "danger"
          :max 25
          :min 0
          :now  (get-in risk-registers [clave])
          :label label}
       :else {:max 25
              :min 0
              :now  (get-in risk-registers [clave])
              :label label})
     {:max 25
       :min 0
       :now 0}))

; Páginas:
(defn risk-register-sumary-page []
  (r/with-let [atomo-risk-registers-local   (subscribe [:risk-registers])]
              [:div.container
               [:div.row
                  [:div.col-sm-12
                    [:h2 "Risk Register"]
                    [buscar-risk-register]]
                  [:div.col-sm-12 [:div.panel.panel-default [tabla-reframe] [control-paginacion] [selector-por-pagina]]
                  [selected-rows-preview]
                   [:div.btn-toolbar.pull-right        [nuevo-rr] [eliminar-rr-group]]]]]
              ))

; Fin Risk Register Sumary---------------------------------------------------------------------------------------------------------------------





; ---------------------------------------------------------------------------------------------------------------------
; Risk Register Detail
; Define la función de renderización de la tabla re-frame. Se incluyen:

(defn pintar-circulo [risk-registers]
  [:div
   (if (or (= (:impact-risk-register rg) nil) (= (:likelihood-risk-register rg) nil))
     (for [rg risk-registers]
       [:h1 [:svg.heat-map

             [componente (:impact-risk-register rg) (:likelihood-risk-register rg) "red"]

             ]]
       )
     [componente 10000 10000 "yellow"])
   ])


(defn componente [x y color] [:circle {:fill color :stroke "black" :r 30 :cx (* x 100) :cy (- 500 (* y 100))}])




;Pagina:

(defn heat-map []
  [:table.table.table-bordered.heat-map
   [:thead
    [:tr
     [:th.cellYellow]
     [:th.cellOrange]
     [:th.cellRed]
     [:th.cellRed]
     [:th.cellRed]
     ]
    ]

   [:thead
    [:tr
     [:th.cellLGreen]
     [:th.cellYellow]
     [:th.cellOrange]
     [:th.cellRed]
     [:th.cellRed]
     ]
    ]

   [:thead
    [:tr
     [:th.cellGreen]
     [:th.cellLGreen]
     [:th.cellYellow]
     [:th.cellOrange]
     [:th.cellRed]
     ]
    ]

   [:thead
    [:tr
     [:th.cellGreen]
     [:th.cellLGreen]
     [:th.cellLGreen]
     [:th.cellYellow]
     [:th.cellOrange]
     ]
    ]

   [:thead
    [:tr
     [:th.cellGreen]
     [:th.cellGreen]
     [:th.cellGreen]
     [:th.cellLGreen]
     [:th.cellYellow]
     ]
    ]]

  )
;(defn svg []
;    ;[:svg.heat-map
;   [pintar-circulo atomo-risk-profile-local]
;  ;[:circle {:fill "Green" :stroke "black" :r 5 :cx 10 :cy 400}]
;   ;(pintar-circulo [])
;   ;[pintar-circulo atomo-risk-profile-local]
;   ;]
;  )

(defn svg-texto []
  [:svg.heat-mapSVG
   [:text {:fill "black" :x -250 :y 30 :transform "rotate(-90 20,20)"} "Probabilidad 0 - 5"]
   ]
  )

(defn svg-texto-horizontal []
  [:svg.heat-mapSVG
   [:text {:fill "black" :x -250 :y 30} "Impacto (0 - 5)"]
   ]
  )


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
                 [:div.col-sm-6
                  [:h4 (:description-risk-register @rr)]
                  [:label "Riesgo: "] [:p (:id-risk @rr)]
                  [:label "Tipo: "] [:p (:id-risk-subtype @rr)]
                  [:label "Localización del riego: "] [:p (:location-risk-register @rr)]
                  [bs/Checkbox
                   {:checked   (boolean (:key-risk-register @rr))}
                   "Riesgo Clave"]
                  [:label "Propietario: "][:p (:owner-risk-register @rr)]
                  [:label "Campaña: "][:p (:id-campaign @rr)]
                   [:ul
                    [:li.risk-detail[:label.risk-small "Valor del riesgo residual: "  (:residual-risk-register @rr)]]
                    [:li.risk-detail[:label.risk-small "Valor del riesgo inherente: "  (:inherent-risk-register @rr)]]
                    [:li.risk-detail[:label.risk-small "Valor del riego continuo: " (:current-risk-register @rr)]]]
                  [:label "Estatus: "][:h3 (:status-risk-register @rr)]
                  [:span.pull-right [bs/Badge (:id-risk-register @rr)]]
                ]
                [:div.col-sm-6
                 [svg-texto][heat-map][pintar-circulo [@rr] ][svg-texto-horizontal]
                 [:div.espacio [:label "Tratamiento: "][:h3 (:id-treatment @rr)]] ]
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

