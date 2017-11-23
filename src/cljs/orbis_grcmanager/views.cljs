(ns orbis-grcmanager.views
  (:require [reagent.core :as r]
            [re-frame.core :refer [subscribe]]
            [orbis-grcmanager.bootstrap :as bs]
            [orbis-grcmanager.routes :refer [context-url href navigate!]]
            [orbis-grcmanager.pages.common :refer [loading-throbber error-modal]]
            [orbis-grcmanager.pages.admin.users :refer [users-page]]
            [orbis-grcmanager.pages.home :refer [home-page]]
            [orbis-grcmanager.pages.issues :refer [edit-issue-page view-issue-page]]
            [orbis-grcmanager.pages.auth :refer [login-page logout]]
            [orbis-grcmanager.pages.riskregister :refer [risk-register-sumary-page risk-register-datail-page]]))


; 5 - Incluir la(s) página(s) asociada(s) en el entorno SPA:
; Una vez creada la página es necesario enmarcarla dentro de la pagina general que
;contiene los distintos elementos (paginas) en un entorno single page application
; Para esto es necesario incluir en views.cljs
;(:require [reagent.core :as r]
;  [re-frame.core :refer [subscribe]]
;  [mi-aplicacion.pages.mi-pagina :refer [mi-pagina]]


(defn nav-link [url title page]
  (let [active-page (subscribe [:active-page])]
    [bs/NavItem {:href (context-url url) :active (= page @active-page)} title]))

(defn navbar [{:keys [admin screenname]}]
  [bs/Navbar
   [bs/Navbar.Header]
   [bs/Navbar.Brand
    [:a#logo (href "/")
     [:span "Seguimientos"]]]
   [bs/Navbar.Collapse
    (when admin
      [bs/Nav
       [nav-link "/users" "Usuarios" :users]])
    [bs/Nav {:pull-right true}
     [bs/NavDropdown
      {:id "logout-menu" :title screenname}
      [bs/MenuItem {:on-click logout} "Logout"]]]]])


; 5A - Incluir la(s) página(s) en la función de rederización:
; El ns views, dinamicamente actualiza la renderización mediante las funciones
; nav-link y nav-bar y las claves de entrada (url, title, page) . Adicionalmente se
; requiere incluir dentro del multi-método pages el método que contendrá como
; parametro la página a renderizar [mi-pagina]
; Esto se logra mediante la inclusión del siguiente comando:
; views.cljs:
; ns mi-applicacion.views
; (defmethod pages :mi-metodo [_ _] [mi-pagina])
; Esto permitirá que al renderizar la función [main-page] se incluya nuestra página


(defmulti pages (fn [page _] page))
(defmethod pages :home [_ _] [home-page])
(defmethod pages :login [_ _] [login-page])
(defmethod pages :users [_ user]
  (if (:admin user)
    [users-page]
    (navigate! "/")))
(defmethod pages :edit-issue [_ _]
  (.scrollTo js/window 0 0)
  [edit-issue-page])
(defmethod pages :view-issue [_ _]
  (.scrollTo js/window 0 0)
  [view-issue-page])
(defmethod pages :default [_ _] [:div])
; Risk Register:
(defmethod pages :risk-register-sumary [_ _]
  (.scrollTo js/window 0 0)
  [risk-register-sumary-page])
(defmethod pages :risk-register-detail [_ _]
  (.scrollTo js/window 0 0)
  [risk-register-datail-page])

; 5B - Lo expuesto en 5A permitirá que al renderizar la función [main-page] se incluya
; nuestra página dinamicammente invocada como parámetro en el metodo pages y en
; combinación con la llave :active-page:
;  (pages @active-page @user)
;  (pages :login nil)


(defn main-page []
  (r/with-let [active-page (subscribe [:active-page])
               user        (subscribe [:user])]
    (if @user
      [:div
       [navbar @user]
       [loading-throbber]
       [error-modal]
       [:div.container.content
        (pages @active-page @user)]]
      (pages :login nil))))
