(ns pangrammer.routes
  (:require [reagent.core :as r]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]            
            [pangrammer.views.main :as main]))

(defn default-view
  "To show before routes are loaded"
  []
  [:h1 "Loading Pangrammer..."])


(def current-view (r/atom default-view) )

(def routes
  (rf/router
   ["/"
    [""
     {:name ::pangrammer
      :view #'main/pangrammer}]]))

(defn init-routes!
  "Start the routing"
  []
  (rfe/start! routes
              (fn [m]
                (reset! current-view (get-in m [:data :view])))
              {:use-fragment true}))
