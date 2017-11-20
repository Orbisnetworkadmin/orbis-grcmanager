(ns orbis-grcmanager.handler
  (:require [compojure.core :refer [routes wrap-routes]]
            [orbis-grcmanager.layout :refer [error-page]]
            [orbis-grcmanager.routes.home :refer [home-routes]]
            [orbis-grcmanager.routes.services :refer [service-routes]]
            [compojure.route :as route]
            [orbis-grcmanager.env :refer [defaults]]
            [mount.core :as mount]
            [orbis-grcmanager.middleware :as middleware]))

(mount/defstate init-app
                :start ((or (:init defaults) identity))
                :stop  ((or (:stop defaults) identity)))

(def app-routes
  (routes
    (wrap-routes #'home-routes middleware/wrap-csrf)
    #'service-routes
    (route/not-found
      (:body
        (error-page {:status 404
                     :title "page not found"})))))


(defn app [] (middleware/wrap-base #'app-routes))
