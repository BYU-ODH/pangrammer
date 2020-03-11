(ns pangrammer.core
  (:require [reagent.core :as r]
            [pangrammer.routes :as routes]))

(defn mount-components []
  ;(r/render [#'navbar] (.getElementById js/document "nav"))
  (r/render [@routes/current-view] (.getElementById js/document "app")))

(defn init! []
  (routes/init-routes!)
  (mount-components))
