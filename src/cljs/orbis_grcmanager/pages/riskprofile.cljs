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
     ::dt/render-fn    semaforo-formatter-inherent
     }

    {::dt/column-key   [:current-risk-register]
     ::dt/column-label "Corriente"
     ::dt/sorting      {::dt/enabled? true}
     ::dt/render-fn    semaforo-formatter-current
     }

    {::dt/column-key   [:residual-risk-register]
     ::dt/column-label "Residual"
     ::dt/sorting      {::dt/enabled? true}
     ::dt/render-fn    semaforo-formatter-residual
     }

    {::dt/column-key   [:status-risk-register]
     ::dt/column-label "Status"
     ::dt/sorting      {::dt/enabled? true}}

    ]
   {::dt/pagination    {::dt/enabled? true
                        ::dt/per-page 10}
    ::dt/table-classes ["table-striped" "table-bordered" "table-hover" "table"],
    ::dt/selection     {::dt/enabled? true}}]
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

(defn semaforo-formatter-residual [_ risk-registers]
  [:div.row>div.col-sm-12
   [bs/Label
    (semaforo :residual-risk-register risk-registers) (texto-etiqueta :residual-risk-register risk-registers)]
   ])

(defn semaforo-formatter-inherent [_ risk-registers]
  [:div.row>div.col-sm-12
   [bs/Label
    (semaforo :inherent-risk-register risk-registers) (texto-etiqueta :inherent-risk-register risk-registers)]
   ])

(defn semaforo-formatter-current [_ risk-registers]
  [:div.row>div.col-sm-12
   [bs/Label
    (semaforo :current-risk-register risk-registers) (texto-etiqueta :current-risk-register risk-registers)]
   ])


(defn texto-etiqueta [clave risk-registers]
  (if (not (= (get-in risk-registers [clave]) nil))
    (str (get-in risk-registers [clave]))
    "vacio"
    ))

(defn semaforo [clave risk-registers]
  (if (not (= (get-in risk-registers [clave]) nil))
    (cond
      (and (> (get-in risk-registers [clave]) 3.99) (< (get-in risk-registers [clave]) 7.00))
      {:bs-style "success"}
      (and (> (get-in risk-registers [clave]) 6.99) (< (get-in risk-registers [clave]) 10.00))
      {:bs-style "info"}
      (and (> (get-in risk-registers [clave]) 9.99) (< (get-in risk-registers [clave]) 13.00))
      {:bs-style "warning"}
      (> (get-in risk-registers [clave]) 12.99)
      {:bs-style "danger"}
      :else {:bs-style "primary"}
      )
    {:bs-style "default"}))


(defn pintar-circulo [risk-registers]
  [:div
   (if (or (= (:impact-risk-register rg) nil) (= (:likelihood-risk-register rg) nil))
     (for [rg risk-registers]
       [:h1 [:svg.heat-map

             [componente (:impact-risk-register rg) (:likelihood-risk-register rg) "white" ]
             ;[componente-texto (:impact-risk-register rg) (:likelihood-risk-register rg) (str (:id-risk-register rg))]
             ]]
       )
     [componente 10000 10000 "yellow"])
   ])

(defn pintar-id [risk-registers]
  [:div
   (if (or (= (:impact-risk-register rg) nil) (= (:likelihood-risk-register rg) nil))
     (for [rg risk-registers]
       [:h1 [:svg.heat-map

             ;[componente (:impact-risk-register rg) (:likelihood-risk-register rg) "white" ]
             [componente-texto (:impact-risk-register rg) (:likelihood-risk-register rg) (str (:id-risk-register rg))]
             ]]
       )
     [componente 10000 10000 "yellow"])
   ])


(defn componente [x y color] [:circle {:fill color :stroke "black" :r 20 :cx (* x 100) :cy (- 500 (* y 100))}])
(defn componente-texto [x y texto] [:text {:fill "black" :x (* x 100) :y (- 500 (* y 100)) :text-anchor "middle" :stroke "black" :stroke-width "1px" :dy ".3em"} texto])


; Páginas:

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

(defn subscription-rows-preview []
  [:pre
   [:code
    (db [:risk-registers])]])




(defn risk-profile-sumary-page []
  (r/with-let [atomo-risk-profile-local @(subscribe [:risk-registers])]
              [:div.container
               [:div.row
                [:div.col-sm-6
                 [svg-texto] [heat-map][pintar-circulo atomo-risk-profile-local] [pintar-id atomo-risk-profile-local] [svg-texto-horizontal]]
                [:div.col-sm-6
                 [svg-texto] [heat-map][pintar-circulo @(subscribe [::dt/selected-items
                                                                     :RegistrodeRiesgos
                                                                     [:risk-registers]])  ] [pintar-id @(subscribe [::dt/selected-items
                                                                                                                                                           :RegistrodeRiesgos
                                                                                                                                                           [:risk-registers]])]  ]]
               [:div.espacio
                [:div.row
                 [:div.col-sm-12
                  [:h2 "Risk Profile"]
                  ; Para pruebas
                  ;[pintar-circulo atomo-risk-profile-local]
                  ;[:h2 (str  (pintar-circulo @(subscribe [::dt/selected-items
                  ;                                         :RegistrodeRiesgos
                  ;                                         [:risk-registers]]) ))]

                  [buscar-risk-register]]
                 [:div.col-sm-12 [:div.panel.panel-default [tabla-reframe] [control-paginacion] [selector-por-pagina]

                                  ]

                  ]]]]))

; Fin Risk Register Sumary---------------------------------------------------------------------------------------------------------------------




