(ns pangrammer.views.main
  (:require [reagent.core :as r]
            [clojure.string :as str]))

(defonce letters-used (r/atom {}))

(defn get-value-from-change [e]
  (.. e -target -value))

(defn char-range
  "Take a range of characters given start and end char"
  [start-char end-char]
  (let [aN (-> start-char (.charCodeAt 0))
        zN (-> end-char (.charCodeAt 0))
        dir (compare zN aN)
        Nrange (if-not (zero? dir)
                 (range aN (+ dir zN) dir)
                 (list zN))]
    (map js/String.fromCharCode Nrange)))
(comment
  (char-range "c" "c") ;; => ("c")
  (char-range "a" "c") ;; => ("a" "b" "c")
  (char-range "c" "a") ;; => ("c" "b" "a")
)

(defonce INPUT (r/atom ""))

(defn letters-so-far
  "Display for letters used so far"
  []
  (into 
   [:div.letters-so-far]
   (for [l (char-range \A \Z)
         :let [lcount (@letters-used l 0)
               color (if (< lcount 1) "red" "green")]]
     [:div.letter {:style {:display "inline-block"
                           :margin-right "1em"}}
      [:span.letter-name {:style {:margin-right "2px"
                                  :border-bottom-width 1.5
                                  :border-bottom-style "solid"
                                  :border-bottom-color color}} l]
      [:span.letter-count {:style {:background-color color}}
       lcount]])))

(defn do-input
  "Input into the text area, updating letters-so-far appropriately"
  [event]
  (let [text1 (-> event get-value-from-change)
        text (str/upper-case text1)
        temp-used (atom {})]
    (reset! INPUT text1)
    (doseq [T text]
      (swap! temp-used update T inc))
    (reset! letters-used @temp-used)))


(defn input-area
  "The input area into which players write"
  []
  [:textarea {:default-value "Input Area"
              :on-change do-input
              :value @INPUT}])

(defn input-count
  "Display the count of input"
  []
  [:div.input-count
   (count @INPUT)])

(defn pangrammer
  "The singular Pangrammer view"
  []
  [:div.content
   [:h1 "Pangrammer 3.0!!!!"]
   [letters-so-far]
   [input-area]
   [input-count]])

