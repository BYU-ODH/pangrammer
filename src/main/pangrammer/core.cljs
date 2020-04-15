(ns pangrammer.core
  (:require [reagent.core :as r]
            [pangrammer.routes :as routes]
            [pangrammer.views.message :as message]))

(defn mount-components []
  (r/render-component [@routes/current-view] (.getElementById js/document "app"))
  (r/render-component [message/message-view] (.getElementById js/document "message")))

(defn init! []
  (routes/init-routes!)
  (mount-components))
