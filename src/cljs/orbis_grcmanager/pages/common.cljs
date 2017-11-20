(ns orbis-grcmanager.pages.common
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch subscribe]]
            [orbis-grcmanager.bootstrap :as bs]))


;


(defn loading-throbber
  []
  (let [loading? (subscribe [:loading?])]
    (when @loading?
      [bs/Modal
       {:show true}
       [bs/Modal.Body
        [:div.spinner
         [:div.bounce1]
         [:div.bounce2]
         [:div.bounce3]]]])))

(defn error-modal []
  (when-let [error @(subscribe [:error])]
    [bs/Modal {:show (boolean error)}
     [bs/Modal.Header
      [bs/Modal.Title "Un error ha ocurrido"]]
     [bs/Modal.Body
      [:p error]]
     [bs/Modal.Footer
      [:button.btn.btn-sm.btn-danger
       {:on-click #(dispatch [:set-error] nil)}
       "OK"]]]))

(defn validation-modal [errors]
  [bs/Modal {:show (boolean @errors)}
   [bs/Modal.Header
    [bs/Modal.Title "Debe completar todos los campos requeridos"]]
   [bs/Modal.Body
    [:ul
     (for [[_ error] @errors]
       ^{:key error}
       [:li error])]]
   [bs/Modal.Footer
    [:button.btn.btn-sm.btn-danger
     {:on-click #(reset! errors nil)}
     "Cerrar"]]])

(defn confirm-modal [title confirm-open? action action-label]
  [bs/Modal {:show @confirm-open?}
   [bs/Modal.Header
    [bs/Modal.Title title]]
   [bs/Modal.Footer
    [:div.btn-toolbar
     [:button.btn.btn-sm.btn-danger
      {:on-click #(reset! confirm-open? false)}
      "Cancelar"]
     [:button.btn.btn-sm.btn-success
      {:on-click #(do
                   (reset! confirm-open? false)
                   (action))}
      action-label]]]])
