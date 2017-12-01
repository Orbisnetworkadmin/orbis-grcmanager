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

(defn validate-create [rr]
  (format-validation-errors
    (b/validate
      (fn [{:keys [path]}]
        ({ [:current-risk-register]   "El riesgo continuio esta vacio o no es un numero positivo"
          [:residual-risk-register]         "El riesgo residual esta vacio o no es un numero positivo"
          [:inherent-risk-register] "El riesgo inherente esta vacio o no es un numero positivo"
          [:description-risk-register] "La descripcion esta vacia"
          [:likelihood-risk-register]  "La probabilidad esta vacio o no es un numero positivo"
          [:impact-risk-register]          "El impacto esta vacio o no es un numero positivo"
          [:id-risk]          "El riesgo padre esta vacio "
          [:id-risk-subtype]          "El tipo de riesgo esta vacio"
          [:id-campaign]          "La campaña esta vacia"
          [:status-risk-register]          "El estatus del riesgo esta vacio"
          [:owner-risk-register]          "El campo dueño esta vacio"
          [:efect-risk-register]          "El efecto esta vacio"
          [:id-treatment]          "El campo tratamiento esta vacio"
          [:location-risk-register]          "El campo ubicación esta vacio"
          }
          path))
      rr
      :current-risk-register   [v/number v/positive]                    ;[v/number v/positive]
      :residual-risk-register  [v/number v/positive]                        ;[v/number v/positive]
      :inherent-risk-register   [v/number v/positive]                            ;[v/number v/positive]
      :description-risk-register v/required
      :likelihood-risk-register  [v/number v/positive]                           ;[v/number v/positive]
      :impact-risk-register  [v/number v/positive]
      :id-risk v/required
      :id-risk-subtype v/required
      :id-campaign v/required
      :status-risk-register v/required
      :owner-risk-register v/required
      :efect-risk-register v/required
      :id-treatment v/required
      :location-risk-register v/required


      )))
