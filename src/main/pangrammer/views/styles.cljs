(ns pangrammer.views.styles
  (:require [garden.core :as garden :refer [css] ]
            [garden.units :as u :refer [px em rem percent]]
            [garden.color :as c :refer [hex->hsl hsl->hex hsl hsla]]))

(defn pangrammer
  "Our stylesheet"
  []
  (css
   (let [hungry-color "red"
         satisfied-color "#90ee90"]
     [:body
      [:.pangrammer-input {}]
      [:div.letter {:display "inline-block"
                    :width (em 2.6)
                    :margin (px 3)
                    :border-color "black"
                    :border-style "solid"
                    :border-width (px 1)
                    :border-radius (px 3)
                    :padding (px 3)}       
       [:&.hungry
        [:.letter-name {:border-bottom-color hungry-color}]
        [:.letter-count {:background-color hungry-color}]]
       [:&.satisfied
        [:letter-name {:border-bottom-color satisfied-color}]
        [:.letter-count {:background-color satisfied-color}]]]
      [:.letter-name {:margin-right (px 2)
                      :font-weight 600
                      :border-bottom-width (px 1.5)
                      :border-bottom-style "solid"}]
      [:.letter-count {:display "inline-block"
                                        ;:height (em 1)
                       :border-radius (px 3)
                       :float "right"
                       :width (em 1)
                       :text-align "center"
                       :vertical-align "middle"
                       }]
      ])))

(defn clear-styles!
  "Remove existing style elements from the document <head>"
  []
  (let [styles (.getElementsByTagName js/document "style")
        style-count (.-length styles)]
    (doseq [n (range style-count 0 -1)]
      (.remove (aget styles (dec n))))))


(defn mount-style
  "Mount the style-element into the header with `style-text`, ensuring this is the only `<style>` in the doc"
  [style-text]
  (let [head (or (.-head js/document)
                 (aget (.getElementsByTagName js/document "head") 0))
        style-el (doto (.createElement js/document "style")
                   (-> .-type (set! "text/css"))
                   (.appendChild (.createTextNode js/document style-text)))]
    (clear-styles!)
    (.appendChild head style-el)))
