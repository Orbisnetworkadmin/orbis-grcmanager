(ns orbis-grcmanager.routes.home
  (:require [orbis-grcmanager.layout :as layout]
            [compojure.core :refer [defroutes GET]]))

; Función para renderizar la plantilla home.html definida en layout
(defn home-page []
  (layout/render "home.html"))

; Rutas que sirven los web services de la aplicación, tambien se incluyen
; las rutas de frontend
;
; Desde ClojureScript:
; 7 -  Adicionalmente deben ser incorporadas las rutas manejadas por secretary en clojurescript
;
(defroutes home-routes
  ; Servicios / Rutas del Core:
  (GET "/" [] (home-page))
  (GET "/search/:query" [] (home-page))
  (GET "/users" [] (home-page))
  (GET "/logs" [] (home-page))
  (GET "/log/:id" [] (home-page))
   ; Servicios / Rutas de la Aplicación:
  (GET "/create-issue" [] (home-page))
  (GET "/edit-issue" [] (home-page))
  (GET "/all-issues" [] (home-page))
  (GET "/recent-issues" [] (home-page))
  (GET "/most-viewed-issues" [] (home-page))
  (GET "/issue/:id" [] (home-page))
  (GET "/issues/:tag-id" [] (home-page))
  (GET "/riskregister" [] (home-page))
  (GET "/riskregister/:id" [] (home-page))
  (GET "/create-rr" [] (home-page))
  (GET "/edit-rr" [] (home-page))
           )

