(ns orbis-grcmanager.app
  (:require [orbis-grcmanager.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
