(ns orbis-grcmanager.env
  (:require [selmer.parser :as parser]
            [clojure.tools.logging :as log]
            [orbis-grcmanager.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[orbis_grcmanager started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[orbis_grcmanager has shut down successfully]=-"))
   :middleware wrap-dev})
