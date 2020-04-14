(ns pangrammer.core
  (:require [reagent.core :as r]
            [pangrammer.routes :as routes]))

(defn default-view []
  [:h1 "This is a default js view!!"])

(defn mount-components []
  ;(r/render [#'navbar] (.getElementById js/document "nav"))
  (r/render-component [@routes/current-view] (.getElementById js/document "app")))

(defn init! []
  (routes/init-routes!)
  (mount-components))
