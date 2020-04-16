(ns pangrammer.views.message
  (:require [reagent.core :as r])
  )

(def MESSAGE (r/atom [:h1.title "A message!"]))

(defn clear-message
  []
  (when @MESSAGE
    (reset! MESSAGE nil)))

(def default-styles
  ["is-info" "is-light"])

(defonce MESSAGE-CLASSES (r/atom default-styles))

(defn message-view
  "The message area to display the content put into the app-db message"
  []
  (when-let [m @MESSAGE] 
    [:div.message.notification {:class @MESSAGE-CLASSES} m]))


