(ns pangrammer.views.main
  (:require [reagent.core :as r]
            [clojure.string :as str]
            [pangrammer.views.styles :as style]
            [pangrammer.views.message :as messages]))

(defonce letters-used (r/atom {}))
(def SORT-BY-COMPLETE? (r/atom true))

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

(defn all-letters
  "Sequence of the letters we care about"
  []
  (char-range \A \Z))

(defn letters-remaining
  []
  (let [all (all-letters)
        letters-with-count (zipmap
                            all
                            (map @letters-used all))]
    (->> letters-with-count
        (filter #(pos? (val %)))
        count
        (- (count all)))))

(defonce INPUT (r/atom ""))

(defn sorted-letters
  "The sequence of letters as sorted by 1) count and 2) alphabetic"
  []
  (let [letters (all-letters)        
        char-value-for-sort (fn [s] (apply - (map (comp js/parseInt #(.charCodeAt % 0)) [\A s])))
        g #(get @letters-used % (char-value-for-sort %))]
    (if @SORT-BY-COMPLETE?
      (sort-by g > letters)
      letters))) 

(defn pangram-complete?
  "Has every letter been used?"
  []
  (let [letters (all-letters)]
    (every? pos? (map @letters-used letters))))

(defn sort-button
  "Button to determine the direction of sorting"
  []
  (let [icon (if @SORT-BY-COMPLETE?
               [:i.fas.fa-sort-amount-up]
               [:i.fas.fa-sort-alpha-down]) ]
    [:a.button.is-info.sort-button {:on-click #(swap! SORT-BY-COMPLETE? not)
                                    :title "Sort alphabetically or by letters used"}
     icon]))


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
     [sort-button]
     score-box]))

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
     [:h1.title "Pangrammer"]
     [:h2.subtitle
      "Pangram: A sentence containing every letter of the alphabet"]]]])

(defn message-pangram-complete
  "Set a message indicating the completion of the pangram"
  []
  (let [complete-message
        [:div.content.box
         (str "Success -- pangram complete in " (count @INPUT) " characters!")]]
    (do 
      (reset! messages/MESSAGE-CLASSES "is-success")
      (reset! messages/MESSAGE complete-message))))

(defn message-letters-remaining
  "Display a message for the number of letters remaining to be used"
  []
  (let [r (letters-remaining)
        one-left? (= 1 r)]
    (if one-left?
      (reset! messages/MESSAGE-CLASSES "is-warning")
      (reset! messages/MESSAGE-CLASSES messages/default-styles))
    (reset! messages/MESSAGE
            [:div.content.box
             (if one-left?
               "Just one letter remaining"
               (str r " letters remaining"))])))


(defn pangrammer
  "The singular Pangrammer view"
  []
  (style/mount-style (style/pangrammer))
  (if (pangram-complete?)
    (message-pangram-complete)
    (message-letters-remaining))
  [:div.container
   [pangrammer-title]  
   [:div.columns
    [:div.column.is-three-quarters
     [input-area]]
    [:div.column.is-one-quarter
     [letters-so-far]]]])

