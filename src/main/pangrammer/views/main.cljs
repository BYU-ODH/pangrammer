(ns pangrammer.views.main
  (:require [reagent.core :as r]
            [clojure.string :as str]
            [pangrammer.views.styles :as style]))

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

(defn sorted-letters
  "The sequence of letters as sorted by 1) count and 2) alphabetic"
  []
  (let [letters (char-range \A \Z)        
        v (fn [s] (apply - (map (comp js/parseInt #(.charCodeAt % 0)) [\A s])))
        g #(get @letters-used % (v %))]
    (sort-by g > letters))) 
(comment
  (sorted-letters))


(defn letters-so-far
  "Display for letters used so far"
  []
  (let [score-box
        (into [:div.content]
              (for [l (sorted-letters)
                    :let [lcount (@letters-used l 0)
                          color-style (if (< lcount 1) "hungry" "satisfied")]]
                [:div.letter {:class color-style}
                 [:span.letter-name l]
                 [:span.letter-count lcount]]))]
    [:div.letters-so-far
     score-box]))
;; sort by 1) count and 2) alphabetical

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

(defn input-count
  "Display the count of input"
  []
  [:div.input-count
   (count @INPUT)])

(defn input-area
  "The input area into which players write"
  []
  [:div.content.box
   [:textarea.textarea.pangrammer-input.is-medium.has-fixed-size
    {:default-value "Input Area"
     :on-change do-input
     :value @INPUT
     :placeholder "Start your pangram"}]
   [input-count]])

(defn pangrammer-title
  "Title Hero for the page"
  []
  [:section.hero
   [:div.hero-body.has-text-centered
    [:div.container
     [:h1.title "Pangrammer"]]]])

(defn pangrammer
  "The singular Pangrammer view"
  []
  (style/mount-style (style/pangrammer))
  [:div.container
   [pangrammer-title]  
   [:div.columns
    [:div.column.is-three-quarters
     [input-area]]
    [:div.column.is-one-quarter
     [letters-so-far]]]
   
   ])

