(ns orbis-grcmanager.core
  (:require [reagent.core :as r]
            [orbis-grcmanager.routes :refer [hook-browser-navigation!]]
            [orbis-grcmanager.ajax :refer [load-interceptors!]]
            [orbis-grcmanager.views :refer [main-page]]
            [orbis-grcmanager.pages.auth :refer [logged-in?]]
            [re-frame.core :refer [dispatch dispatch-sync]]
            [re-frisk.core :as re-frisk]
    ;;initialize handlers and subscriptions
            orbis-grcmanager.handlers
            orbis-grcmanager.subscriptions))

(defn mount-components []
  (r/render [#'main-page] (.getElementById js/document "app")))

(defn init! []
  (dispatch-sync [:initialize-db])
  (re-frisk/enable-re-frisk!)
  (if (logged-in?) (dispatch [:run-login-events]))
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))
