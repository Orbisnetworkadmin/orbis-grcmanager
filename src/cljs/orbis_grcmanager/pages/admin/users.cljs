(ns orbis-grcmanager.pages.admin.users
  (:require [orbis-grcmanager.bootstrap :as bs]
            [re-frame.core :refer [dispatch subscribe]]
            [orbis-grcmanager.key-events :refer [on-enter]]
            [orbis-grcmanager.pages.common :refer [validation-modal]]
            [orbis-grcmanager.validation :as v]
            [reagent.core :as r]))

(defn user-search []
  (r/with-let [search    (r/atom nil)
               do-search #(when-let [value (not-empty @search)]
                           (dispatch [:admin/search-for-users value]))]
    [bs/FormGroup
     [bs/InputGroup
      [bs/FormControl
       {:type        "text"
        :class       "input-sm"
        :placeholder "Introduzca un username para ver el detalle"
        :on-change   #(reset! search (-> % .-target .-value))
        :on-key-down #(on-enter % do-search)}]
      [bs/InputGroup.Button
       [:button.btn.btn-sm.btn-default
        {:on-click do-search}
        "Buscar"]]]]))

(defn control-buttons [user close-editor]
  (r/with-let [errors  (r/atom nil)
               user-id (:user-id @user)]
    [:div.row>div.col-sm-12
     [validation-modal errors]
     [:div.pull-right
      [:div.btn-toolbar
       [:button.btn.btn-sm.btn-danger
        {:on-click close-editor}
        "Cancelar"]
       [:button.btn.btn-sm.btn-success
        {:pull-right true
         :on-click   #(let [new-user? (nil? user-id)]
                       (when-not (reset! errors
                                         ((if new-user?
                                            v/validate-create-user
                                            v/validate-update-user)
                                           @user))
                         (dispatch
                           [(if new-user?
                              :admin/create-user-profile
                              :admin/update-user-profile)
                            @user])
                         (close-editor)))}
        "Guardar"]]]]))

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

(defn edit-user [title user-map close-editor]
  (r/with-let [user (-> user-map
                        (dissoc :last-login)
                        (update :pass identity)
                        (update :pass-confirm identity)
                        (update :admin boolean)
                        (update :is-active boolean)
                        r/atom)]
    [:div.form-horizontal
     [:legend title]
     [field-group
      "Screen Name"
      (r/cursor user [:screenname])
      :text "Introduzca el username"]
     [field-group
      "Password"
      (r/cursor user [:pass])
      :password
      (if (:last-login user-map)
        "Introduzca el password (dejar en blanco para usar la misma contrasena)"
        "Introduzca el password")]
     [field-group
      "Confirm password"
      (r/cursor user [:pass-confirm])
      :password "Confirme el password"]
     [bs/FormGroup
      [:span.col-lg-2]
      [:div.col-lg-10
       [bs/Checkbox
        {:checked   (boolean (:admin @user))
         :on-change #(swap! user update :admin not)}
        "Admin"]
       [bs/Checkbox
        {:checked   (boolean (:is-active @user))
         :on-change #(swap! user update :is-active not)}
        "Activo"]]]
     [control-buttons user close-editor]]))

(defn user-info [user-map]
  (r/with-let [expanded? (r/atom false)]
    [bs/ListGroupItem
     (if @expanded?
       [edit-user "Editar Usuario" user-map #(reset! expanded? false)]
       [:div
        [:span (:screenname user-map)]
        [:button.btn.btn-xs.btn-primary.pull-right
         {:on-click #(swap! expanded? not)}
         "Editar"]])]))

(defn user-list []
  (let [users (subscribe [:admin/users])]
    (when-not (empty? @users)
      [bs/ListGroup
       (for [user @users]
         ^{:key (:user-id user)}
         [user-info user])])))

(defn users-page []
  (r/with-let [show-new-user-form? (r/atom false)]
    (if @show-new-user-form?
      [:div.row
       [:div.col-sm-12
        [edit-user "Anadir Usuario" {} #(reset! show-new-user-form? false)]]]
      [:div
       [:div.row
        [:div.col-sm-10 [user-search]]
        [:div.col-sm-2
         [:button.btn.btn-sm.btn-success.pull-right
          {:on-click #(reset! show-new-user-form? true)}
          "Anadir nuevo usuario"]]]
       [user-list]])))

