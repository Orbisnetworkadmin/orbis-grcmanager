(ns orbis-grcmanager.pages.home
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe]]
            [orbis-grcmanager.pages.issues :refer [markdown-preview]]
            [orbis-grcmanager.key-events :refer [on-enter]]
            [orbis-grcmanager.bootstrap :as bs]
            [orbis-grcmanager.routes :refer [href navigate!]]
            [re-com.core
             :refer [box v-box h-split v-split title flex-child-style input-text input-textarea]]))


; 4 - Crear la(s) página(s) asociada(s):
; Se define una función que contenga el html embebido con re-agent, aca queda el contenido
; gráfico de las paginas y es llamado desde otras partes de la aplicación:
; pages/mi-pagina.cljs
; ns pages.mi-pagina.cljs
; defn mi-pagina []
;(r/with-let [atomo-local (subscribe [:estructura-de-datos])]
;            [:div.container
;                 [:h1 (:mi-campo @atomo-local)]] ) )
;Observese dentro de la función whit-let de re-agent se utiliza u atomo-local
; asociado al evento previamente subscrito, destacando que esta suscrición contiene
; todos los datos entregados por la mi-api-swagger, esto con la finalidad de manejar
; localmente los datos en la página (atomo-local)

(defn issue-search []
  (r/with-let [search (r/atom nil)
               do-search #(when-let [value (not-empty @search)]
                            (navigate! (str "/search/" value)))]
              [bs/FormGroup
               [bs/InputGroup
                [bs/FormControl
                 {:type        "text"
                  :class       "input-sm"
                  :placeholder "Introduzca el detalle del Plan"
                  :on-change   #(reset! search (-> % .-target .-value))
                  :on-key-down #(on-enter % do-search)}]
                [bs/InputGroup.Button
                 [:button.btn.btn-sm.btn-default
                  {:on-click do-search}
                  "Buscar"]]]]))

(defn new-issue []
  [:a.btn.btn-sm.btn-success.pull-right
   (href "/create-issue") "Agregar"])

(defn issue-panel [{:keys [support-issue-id title summary views]}]
  [:div.panel.panel-default
   [:div.panel-heading.issue-title
    [:h3>a (href (str "/issue/" support-issue-id))
     title
     [:span.pull-right [bs/Badge views]]]]
   [:div.panel-body summary]])

(defn sorted-tags [tags sort-type]
  (let [tags (filter #(pos? (:tag-count %)) tags)]
    (case sort-type
      :name (sort-by :tag tags)

      :count (->> tags
                  (sort-by :tag-count)
                  (reverse)))))

(defn tag-control [title count selected on-click]
  [bs/ListGroupItem
   {:on-click on-click
    :active   (= title selected)}
   [:b title] " "
   (when count [bs/Badge count])])

(defn tags-panel [tags selected]
  (r/with-let [sort-type (r/atom :count)]
              [:div
               [:h2 "Riesgos"]
               [:ul.nav.nav-tabs
                [:li {:class (when (= @sort-type :count) "active")}
                 [:a {:on-click #(reset! sort-type :count)}
                  "# Riesgo"]]
                [:li {:class (when (= @sort-type :name) "active")}
                 [:a {:on-click #(reset! sort-type :name)}
                  "A-Z"]]]
               [:div.panel
                [bs/ListGroup
                 (for [{:keys [tag-id tag tag-count]} (sorted-tags tags @sort-type)]
                   ^{:key tag-id}
                   [tag-control tag tag-count selected #(navigate! (str "/issues/" tag))])]]]))

(defn filter-control [title selected on-click]
  [:button.btn.btn-xs
   {:on-click on-click
    :class    (if (= title selected) "btn-success" "btn-default")}
   title])

(defn filters [selected]
  [:div.btn-toolbar.filters
   [filter-control
    "Todos"
    selected
    #(navigate! "/all-issues")]
   [filter-control
    "Recent"
    selected
    #(navigate! "/recent-issues")]
   [filter-control
    "Más vistos"
    selected
    #(navigate! "/most-viewed-issues")]
   (when-not (contains? #{"Todos" "Recent" "Más vistos"} selected)
     [:button.btn.btn-xs.btn-success selected])])

(defn home-page []
  (r/with-let [tags (subscribe [:tags])
               issues (subscribe [:issues])
               selected (subscribe [:selected-tag])]
              [:div.container
               [:div.row
                [:div.col-sm-3
                 [tags-panel @tags @selected]]
                [:div.col-sm-9
                 [:h2 "Planes de Acción"
                  [filters @selected]
                  [new-issue]]
                 [issue-search]
                 (for [issue-summary @issues]
                   ^{:key (:support-issue-id issue-summary)}
                   [issue-panel issue-summary])]]]))
