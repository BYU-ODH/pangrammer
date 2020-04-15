(ns pangrammer.views.message
  (:require [reagent.core :as r])
  )

(def MESSAGE (r/atom [:h1.title "A message!"]))

(defn message-view
  "The message area to display the content put into the app-db message"
  []
  (when-let [m @MESSAGE] 
    [:div.message m]))

