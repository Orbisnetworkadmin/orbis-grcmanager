(ns orbis-grcmanager.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[orbis_grcmanager started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[orbis_grcmanager has shut down successfully]=-"))
   :middleware identity})
