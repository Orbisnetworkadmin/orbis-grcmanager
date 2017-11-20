(ns user
  (:require [mount.core :as mount]
            [orbis-grcmanager.figwheel :refer [start-fw stop-fw cljs]]
            orbis-grcmanager.core))

(defn start []
  (mount/start-without #'orbis-grcmanager.core/repl-server))

(defn stop []
  (mount/stop-except #'orbis-grcmanager.core/repl-server))

(defn restart []
  (stop)
  (start))


