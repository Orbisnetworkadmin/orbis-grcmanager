(ns orbis-grcmanager.validation
  (:require [bouncer.core :as b]
            [bouncer.validators :as v]))

(defn format-validation-errors [errors]
  (->> errors
       first
       (map (fn [[k [v]]] [k v]))
       (into {})
       not-empty))

(defn validate-issue [issue]
  (format-validation-errors
    (b/validate
      (fn [{:keys [path]}]
        ({[:title]   "El titulo del seguimiento es requerido"
          [:summary] "El resumen del seguimiento es requerido"
          [:detail]  "El detalle del seguimiento es requerido"
          [:tags]    "El seguimiento debe tener al menos un tag"}
          path))
      issue
      :title v/required
      :summary v/required
      :detail v/required
      :tags [v/required [v/min-count 1]])))

(defn pass-matches? [pass-confirm pass]
  (= pass pass-confirm))

(defn validate-create-user [user]
  (format-validation-errors
    (b/validate
      (fn [{:keys [path]}]
        ({[:screenname]   "El perfil de usuario es requerido"
          [:pass]         "Password de 8+ caracteres es requerido"
          [:pass-confirm] "Los Passwords no coinciden"
          [:is-admin]     "Debe especificar si el usuario es un administrador"
          [:active]       "Debe especificar si el usuario esta activo"}
          path))
      user
      :pass [v/required [v/min-count 8]]
      :pass-confirm [[pass-matches? (:pass user)]]
      :screenname v/required
      :admin v/required
      :is-active v/required)))

(defn validate-update-user [user]
  (format-validation-errors
    (b/validate
      (fn [{:keys [path]}]
        ({[:screenname]   "El perfil de usuario es requerido"
          [:is-admin]     "Debe especificar si el usuario es un administrador"
          [:pass-confirm] "Los Passwords no coinciden"
          [:active]       "Debe especificar si el usuario esta activo"}
          path))
      user
      :screenname v/required
      :admin v/required
      :pass-confirm [[pass-matches? (:pass user)]]
      :is-active v/required)))
