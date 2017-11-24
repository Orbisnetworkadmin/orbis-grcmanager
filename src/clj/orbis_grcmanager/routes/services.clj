(ns orbis-grcmanager.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [compojure.api.upload :refer [TempFileUpload wrap-multipart-params]]
            [schema.core :as s]
            [compojure.api.meta :refer [restructure-param]]
            [orbis-grcmanager.routes.services.attachments :as attachments]
            [orbis-grcmanager.routes.services.issues :as issues]
            [orbis-grcmanager.routes.services.auth :as auth]
            [orbis-grcmanager.routes.services.riskregister :as riskregister]
            [orbis-grcmanager.routes.services.logs :as logs]
            [buddy.auth.accessrules :refer [restrict]]
            [buddy.auth :refer [authenticated?]])
  (:import java.util.Date))

(defn admin?
  [request]
  (:admin (:identity request)))

(defn access-error [_ _]
  (unauthorized {:error "unauthorized / no autorizado"}))

(defn wrap-restricted [handler rule]
  (restrict handler {:handler  rule
                     :on-error access-error}))

(defmethod restructure-param :auth-rules
  [_ rule acc]
  (update-in acc [:middleware] conj [wrap-restricted rule]))

(defmethod restructure-param :current-user
  [_ binding acc]
  (update-in acc [:letks] into [binding `(:identity ~'+compojure-api-request+)]))




; 4- Web Services:
; Se definen los distintos metodos de consulta al web service y se agrupan, bajo el tag "private" residen
; las APIs que requieren CSRF Token y autenticación.

(defapi service-routes
  {:swagger {:ui   "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version     "1.0.0"
                           :title       "Orbis GRCManager API"
                           :description "Orbis GRCManager Web Services"}}}}

  ; Servicios del core:

  (POST "/api/login" req
    :return auth/LoginResponse
    :body-params [userid :- s/Str
                  pass :- s/Str]
    :summary "user login handler / login del usuario"
    (auth/login userid pass req))

  ; Contexto /Admin
  (context "/admin" []
    :auth-rules admin?
    :tags ["admin"]

    (GET "/users/:screenname" []
      :path-params [screenname :- s/Str]
      :return auth/SearchResponse
      :summary "returns users with matching screennames / devuelve los usuarios que coincidan con el perfil (screenname)"
      (auth/find-users screenname))

    (POST "/user" []
      :body-params [screenname :- s/Str
                    pass :- s/Str
                    pass-confirm :- s/Str
                    admin :- s/Bool
                    is-active :- s/Bool]
      (auth/register! {:screenname   screenname
                       :pass         pass
                       :pass-confirm pass-confirm
                       :admin        admin
                       :is-active    is-active}))

    (PUT "/user" []
      :body-params [user-id :- s/Int
                    screenname :- s/Str
                    pass :- (s/maybe s/Str)
                    pass-confirm :- (s/maybe s/Str)
                    admin :- s/Bool
                    is-active :- s/Bool]
      :return auth/LoginResponse
      (auth/update-user! {:user-id      user-id
                          :screenname   screenname
                          :pass         pass
                          :pass-confirm pass-confirm
                          :admin        admin
                          :is-active    is-active}))


    (DELETE "/logs" []
      :path-params [screenname :- s/Str]
      :body-params [id_user             :-           s/Num
                    hostname_log        :-           s/Str
                    resource_log        :-           s/Num
                    severity_log        :-           s/Num
                    message_log         :-           s/Str]
      :return s/Int
      :summary "delete all logs / elimina todo el log de auditoria, reuiere de screenname para registrar la acción"
      (logs/delete-all-logs! screenname)))


  ; Contexto /API Private
  (context "/api" []
    :auth-rules authenticated?
    :tags ["private"]

    ;; Logout
    (POST "/logout" []
      :return auth/LogoutResponse
      :summary "remove the user from the session / remueve el usuario de la sessión"
      (auth/logout))

    ;; Risk Register
    (GET "/riskregisters" []
      :return riskregister/RRSummaryResults
      :summary "Lista todos los Registros de Riesgos de la aplicación"
      (riskregister/get-all-riskregisters))


    (GET "/riskregister/:id-risk-register" []
      :path-params [id-risk-register :- s/Int]
      :return riskregister/RiskRegisterResponse
      :summary "Devuelve un Risk Register dado el ID"
      (riskregister/risk-register-by-id {:id-risk-register id-risk-register}))


    (POST "/riskregister" []
      :body-params [id-risk                        :-   s/Str
                    id-risk-subtype                :-   s/Str
                    id-campaign                    :-   s/Str
                    status-risk-register           :-   s/Str
                    owner-risk-register            :-   s/Str
                    description-risk-register      :-   s/Str
                    efect-risk-register            :-   s/Str
                    location-risk-register         :-   s/Str
                    id-treatment                   :-   s/Str
                    key-risk-register              :-   s/Bool
                    likelihood-risk-register       :-   s/Num
                    impact-risk-register           :-   s/Num
                    inherent-risk-register         :-   s/Num
                    current-risk-register          :-   s/Num
                    ecd-risk-register              :-   s/Num
                    ece-risk-register              :-   s/Num
                    residual-risk-register         :-   s/Num
                    startdate-identificacion       :-   (s/maybe s/Str)
                    enddate-identificacion         :-   (s/maybe s/Str)
                    technique-identificacion       :-   (s/maybe s/Str)
                    status-identificacion          :-   (s/maybe s/Str)
                    startdate-analisis             :-   (s/maybe s/Str)
                    enddate-analisis               :-   (s/maybe s/Str)
                    technique-analisis             :-   (s/maybe s/Str)
                    status-analisis                :-   (s/maybe s/Str)
                    startdate-evaluacion           :-   (s/maybe s/Str)
                    enddate-evaluacion             :-   (s/maybe s/Str)
                    technique-evaluacion           :-   (s/maybe s/Str)
                    status-evaluacion              :-   (s/maybe s/Str)
                    startdate-tratamiento          :-   (s/maybe s/Str)
                    enddate-tratamiento            :-   (s/maybe s/Str)
                    technique-tratamiento          :-   (s/maybe s/Str)
                    status-tratamiento             :-   (s/maybe s/Str)
                    startdate-monitoreo            :-   (s/maybe s/Str)
                    enddate-monitoreo              :-   (s/maybe s/Str)
                    technique-monitoreo            :-   (s/maybe s/Str)
                    status-monitoreo               :-   (s/maybe s/Str)
                    kri-risk-register-title        :-   (s/maybe s/Str)
                    kri-risk-register-descritpion  :-   (s/maybe s/Str)]

      :summary "adds a new risk register / anade un nuevo registro de riesgo (Risk Register)"
      (riskregister/insert-risk-register!
        { :id-risk                            id-risk
          :id-risk-subtype                    id-risk-subtype
          :id-campaign                        id-campaign
          :status-risk-register               status-risk-register
          :owner-risk-register                owner-risk-register
          :description-risk-register          description-risk-register
          :efect-risk-register                efect-risk-register
          :location-risk-register             location-risk-register
          :id-treatment                       id-treatment
          :key-risk-register                  key-risk-register
          :likelihood-risk-register           likelihood-risk-register
          :impact-risk-register               impact-risk-register
          :inherent-risk-register             inherent-risk-register
          :current-risk-register              current-risk-register
          :ecd-risk-register                  ecd-risk-register
          :ece-risk-register                  ece-risk-register
          :residual-risk-register             residual-risk-register
          :startdate-identificacion           startdate-identificacion
          :enddate-identificacion             enddate-identificacion
          :technique-identificacion           technique-identificacion
          :status-identificacion              status-identificacion
          :startdate-analisis                 startdate-analisis
          :enddate-analisis                   enddate-analisis
          :technique-analisis                 technique-analisis
          :status-analisis                    status-analisis
          :startdate-evaluacion               startdate-evaluacion
          :enddate-evaluacion                 enddate-evaluacion
          :technique-evaluacion               technique-evaluacion
          :status-evaluacion                  status-evaluacion
          :startdate-tratamiento              startdate-tratamiento
          :enddate-tratamiento                enddate-tratamiento
          :technique-tratamiento              technique-tratamiento
          :status-tratamiento                 status-tratamiento
          :startdate-monitoreo                startdate-monitoreo
          :enddate-monitoreo                  enddate-monitoreo
          :technique-monitoreo                technique-monitoreo
          :status-monitoreo                   status-monitoreo
          :kri-risk-register-title            kri-risk-register-title
          :kri-risk-register-descritpion      kri-risk-register-descritpion  }))

    (PUT "/riskregister" []
      :body-params [id-risk-register               :-   s/Int
                    id-risk                        :-   s/Str
                    id-risk-subtype                :-   s/Str
                    id-campaign                    :-   s/Str
                    status-risk-register           :-   s/Str
                    owner-risk-register            :-   s/Str
                    description-risk-register      :-   s/Str
                    efect-risk-register            :-   s/Str
                    location-risk-register         :-   s/Str
                    id-treatment                   :-   s/Str
                    key-risk-register              :-   s/Bool
                    likelihood-risk-register       :-   s/Num
                    impact-risk-register           :-   s/Num
                    inherent-risk-register         :-   s/Num
                    current-risk-register          :-   s/Num
                    ecd-risk-register              :-   s/Num
                    ece-risk-register              :-   s/Num
                    residual-risk-register         :-   s/Num
                    startdate-identificacion       :-   (s/maybe s/Str)
                    enddate-identificacion         :-   (s/maybe s/Str)
                    technique-identificacion       :-   (s/maybe s/Str)
                    status-identificacion          :-   (s/maybe s/Str)
                    startdate-analisis             :-   (s/maybe s/Str)
                    enddate-analisis               :-   (s/maybe s/Str)
                    technique-analisis             :-   (s/maybe s/Str)
                    status-analisis                :-   (s/maybe s/Str)
                    startdate-evaluacion           :-   (s/maybe s/Str)
                    enddate-evaluacion             :-   (s/maybe s/Str)
                    technique-evaluacion           :-   (s/maybe s/Str)
                    status-evaluacion              :-   (s/maybe s/Str)
                    startdate-tratamiento          :-   (s/maybe s/Str)
                    enddate-tratamiento            :-   (s/maybe s/Str)
                    technique-tratamiento          :-   (s/maybe s/Str)
                    status-tratamiento             :-   (s/maybe s/Str)
                    startdate-monitoreo            :-   (s/maybe s/Str)
                    enddate-monitoreo              :-   (s/maybe s/Str)
                    technique-monitoreo            :-   (s/maybe s/Str)
                    status-monitoreo               :-   (s/maybe s/Str)
                    kri-risk-register-title        :-   (s/maybe s/Str)
                    kri-risk-register-descritpion  :-   (s/maybe s/Str)]
      :return s/Int
      :summary "update an existing risk register / actualiza un risk register"
      (riskregister/update-risk-register!
        {:id-risk-register                   id-risk-register
         :id-risk                            id-risk
         :id-risk-subtype                    id-risk-subtype
         :id-campaign                        id-campaign
         :status-risk-register               status-risk-register
         :owner-risk-register                owner-risk-register
         :description-risk-register          description-risk-register
         :efect-risk-register                efect-risk-register
         :location-risk-register             location-risk-register
         :id-treatment                       id-treatment
         :key-risk-register                  key-risk-register
         :likelihood-risk-register           likelihood-risk-register
         :impact-risk-register               impact-risk-register
         :inherent-risk-register             inherent-risk-register
         :current-risk-register              current-risk-register
         :ecd-risk-register                  ecd-risk-register
         :ece-risk-register                  ece-risk-register
         :residual-risk-register             residual-risk-register
         :startdate-identificacion           startdate-identificacion
         :enddate-identificacion             enddate-identificacion
         :technique-identificacion           technique-identificacion
         :status-identificacion              status-identificacion
         :startdate-analisis                 startdate-analisis
         :enddate-analisis                   enddate-analisis
         :technique-analisis                 technique-analisis
         :status-analisis                    status-analisis
         :startdate-evaluacion               startdate-evaluacion
         :enddate-evaluacion                 enddate-evaluacion
         :technique-evaluacion               technique-evaluacion
         :status-evaluacion                  status-evaluacion
         :startdate-tratamiento              startdate-tratamiento
         :enddate-tratamiento                enddate-tratamiento
         :technique-tratamiento              technique-tratamiento
         :status-tratamiento                 status-tratamiento
         :startdate-monitoreo                startdate-monitoreo
         :enddate-monitoreo                  enddate-monitoreo
         :technique-monitoreo                technique-monitoreo
         :status-monitoreo                   status-monitoreo
         :kri-risk-register-title            kri-risk-register-title
         :kri-risk-register-descritpion      kri-risk-register-descritpion  }))


    (DELETE "/riskregister/:id-risk-register" []
      :path-params [id-risk-register :- s/Int]
      :return s/Int
      :summary "delete the Risk Register with the given id / elimina un registro de riesgos (Risk Register) dado el id"
      (riskregister/delete-risk-register! {:id-risk-register id-risk-register}))


    (PUT "/riskregistergroup/" []
      :body-params [id-risk-register               :-   s/Int
                    id-risk                        :-   s/Str
                    id-risk-subtype                :-   s/Str
                    id-campaign                    :-   s/Str
                    status-risk-register           :-   s/Str
                    owner-risk-register            :-   s/Str
                    description-risk-register      :-   s/Str
                    efect-risk-register            :-   s/Str
                    location-risk-register         :-   s/Str
                    id-treatment                   :-   s/Str
                    key-risk-register              :-   s/Bool
                    likelihood-risk-register       :-   s/Num
                    impact-risk-register           :-   s/Num
                    inherent-risk-register         :-   s/Num
                    current-risk-register          :-   s/Num
                    ecd-risk-register              :-   s/Num
                    ece-risk-register              :-   s/Num
                    residual-risk-register         :-   s/Num
                    startdate-identificacion       :-   (s/maybe s/Str)
                    enddate-identificacion         :-   (s/maybe s/Str)
                    technique-identificacion       :-   (s/maybe s/Str)
                    status-identificacion          :-   (s/maybe s/Str)
                    startdate-analisis             :-   (s/maybe s/Str)
                    enddate-analisis               :-   (s/maybe s/Str)
                    technique-analisis             :-   (s/maybe s/Str)
                    status-analisis                :-   (s/maybe s/Str)
                    startdate-evaluacion           :-   (s/maybe s/Str)
                    enddate-evaluacion             :-   (s/maybe s/Str)
                    technique-evaluacion           :-   (s/maybe s/Str)
                    status-evaluacion              :-   (s/maybe s/Str)
                    startdate-tratamiento          :-   (s/maybe s/Str)
                    enddate-tratamiento            :-   (s/maybe s/Str)
                    technique-tratamiento          :-   (s/maybe s/Str)
                    status-tratamiento             :-   (s/maybe s/Str)
                    startdate-monitoreo            :-   (s/maybe s/Str)
                    enddate-monitoreo              :-   (s/maybe s/Str)
                    technique-monitoreo            :-   (s/maybe s/Str)
                    status-monitoreo               :-   (s/maybe s/Str)
                    kri-risk-register-title        :-   (s/maybe s/Str)
                    kri-risk-register-descritpion  :-   (s/maybe s/Str)]
      :return s/Int
      :summary "delete a  Risk Register group with the given id / elimina un grupo de registros de riesgos (Risk Register) dado el id"
      (riskregister/delete-risk-register-group! [{:id-risk-register            id-risk-register
                                           :id-risk                            id-risk
                                           :id-risk-subtype                    id-risk-subtype
                                           :id-campaign                        id-campaign
                                           :status-risk-register               status-risk-register
                                           :owner-risk-register                owner-risk-register
                                           :description-risk-register          description-risk-register
                                           :efect-risk-register                efect-risk-register
                                           :location-risk-register             location-risk-register
                                           :id-treatment                       id-treatment
                                           :key-risk-register                  key-risk-register
                                           :likelihood-risk-register           likelihood-risk-register
                                           :impact-risk-register               impact-risk-register
                                           :inherent-risk-register             inherent-risk-register
                                           :current-risk-register              current-risk-register
                                           :ecd-risk-register                  ecd-risk-register
                                           :ece-risk-register                  ece-risk-register
                                           :residual-risk-register             residual-risk-register
                                           :startdate-identificacion           startdate-identificacion
                                           :enddate-identificacion             enddate-identificacion
                                           :technique-identificacion           technique-identificacion
                                           :status-identificacion              status-identificacion
                                           :startdate-analisis                 startdate-analisis
                                           :enddate-analisis                   enddate-analisis
                                           :technique-analisis                 technique-analisis
                                           :status-analisis                    status-analisis
                                           :startdate-evaluacion               startdate-evaluacion
                                           :enddate-evaluacion                 enddate-evaluacion
                                           :technique-evaluacion               technique-evaluacion
                                           :status-evaluacion                  status-evaluacion
                                           :startdate-tratamiento              startdate-tratamiento
                                           :enddate-tratamiento                enddate-tratamiento
                                           :technique-tratamiento              technique-tratamiento
                                           :status-tratamiento                 status-tratamiento
                                           :startdate-monitoreo                startdate-monitoreo
                                           :enddate-monitoreo                  enddate-monitoreo
                                           :technique-monitoreo                technique-monitoreo
                                           :status-monitoreo                   status-monitoreo
                                           :kri-risk-register-title            kri-risk-register-title
                                           :kri-risk-register-descritpion      kri-risk-register-descritpion  }]))


    ;; Logs
    (GET "/logs" []
      :return logs/LogSummaryResults
      :summary "Lista todos los eventos registrados en el log de la aplicación"
      (logs/get-all-logs))


    (POST "/log" []
      :current-user user
      :body-params [title :- s/Str
                    summary :- s/Str
                    detail :- s/Str
                    tags :- [s/Str]]
      :return s/Int
      :summary "adds a new log record / anade un nuevo registro al log de eventos"
      (logs/insert-log!
        { :id_user                         s/Num
          :hostname_log                    (:user-id user)
          :resource_log                    s/Num
          :severity_log                    s/Num
          :message_log                     s/Str}))




    ;;tags
    (GET "/tags" []
      :return issues/TagsResult
      :summary "list available tags / lista las tags disponibles"
      (issues/tags))

    (POST "/tag" []
      :body-params [tag :- s/Str]
      :return issues/TagResult
      :summary "add a new tag / anade un tag nuevo"
      (issues/add-tag! {:tag tag}))

    ;;issues
    (GET "/issues" []
      :return issues/IssueSummaryResults
      :summary "list all issues / lista todos los seguimientos"
      (issues/all-issues))

    (GET "/recent-issues" []
      :return issues/IssueSummaryResults
      :summary "list 10 most recent issues / lista los 10 seguimientos mas recientes"
      (issues/recent-issues 10))

    (GET "/issues-by-views/:offset/:limit" []
      :path-params [offset :- s/Int limit :- s/Int]
      :return issues/IssueSummaryResults
      :summary "list issues by views using the given offset and limit / lista los seguimientos mas vistos dado el offset y limit"
      (issues/issues-by-views {:offset offset :limit limit}))

    (GET "/issues-by-tag/:tag" []
      :path-params [tag :- s/Str]
      :return issues/IssueSummaryResults
      :summary "list issues by the given tag / lista los seguimientos dado un tag"
      (issues/issues-by-tag {:tag tag}))

    (DELETE "/issue/:id" []
      :path-params [id :- s/Int]
      :return s/Int
      :summary "delete the issue with the given id / elimina un seguimiento dado el id"
      (issues/delete-issue! {:support-issue-id id}))

    (POST "/search-issues" []
      :body-params [query :- s/Str
                    limit :- s/Int
                    offset :- s/Int]
      :return issues/IssueSummaryResults
      :summary "search for issues matching the query / busca seguimientos segun la consulta"
      (issues/search-issues {:query  query
                             :limit  limit
                             :offset offset}))

    (GET "/issue/:id" []
      :path-params [id :- s/Int]
      :return issues/IssueResult
      :summary "returns the issue with the given id / devuelve un seguimiento dado el id"
      (issues/issue {:support-issue-id id}))

    (POST "/issue" []
      :current-user user
      :body-params [title :- s/Str
                    summary :- s/Str
                    detail :- s/Str
                    tags :- [s/Str]]
      :return s/Int
      :summary "adds a new issue / anade un nuevo seguimiento"
      (issues/add-issue!
        {:title   title
         :summary summary
         :detail  detail
         :tags    tags
         :user-id (:user-id user)}))

    (PUT "/issue" []
      :current-user user
      :body-params [support-issue-id :- s/Int
                    title :- s/Str
                    summary :- s/Str
                    detail :- s/Str
                    tags :- [s/Str]]
      :return s/Int
      :summary "update an existing issue / actualiza un seguimiento"
      (issues/update-issue!
        {:support-issue-id support-issue-id
         :title            title
         :summary          summary
         :detail           detail
         :tags             tags
         :user-id          (:user-id user)}))

    ;;attachments
    (POST "/attach-file" []
      :multipart-params [support-issue-id :- s/Int
                         file :- TempFileUpload]
      :middleware [wrap-multipart-params]
      :summary "handles file upload / subida de archivos"
      :return attachments/AttachmentResult
      (attachments/attach-file-to-issue! support-issue-id file))

    (GET "/file/:support-issue-id/:name" []
      :summary "load a file from the database matching the support issue id and the filename / carrga de archivos segun id"
      :path-params [support-issue-id :- s/Int
                    name :- s/Str]
      (attachments/load-file-data {:support-issue-id support-issue-id
                                   :name             name}))

    (DELETE "/file/:support-issue-id/:name" []
      :summary "delete a file from the database / elimina archivo"
      :path-params [support-issue-id :- s/Int
                    name :- s/Str]
      :return attachments/AttachmentResult
      (attachments/remove-file-from-issue! {:support-issue-id support-issue-id
                                            :name             name}))))
