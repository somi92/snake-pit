(ns snake-pit.gui.main
  (:use seesaw.core)
  (:use snake-pit.gui.breed))

(def gap-ho [:fill-h 5])
(def gap-ve [:fill-v 2])

(def main-frame-content
  (border-panel
   :north (vertical-panel
           :items [gap-ve (horizontal-panel :items
                                           [gap-ho (label "Application of genetic programming to a snake game.") gap-ho])])
   :center (vertical-panel :items [gap-ve (horizontal-panel :items [gap-ho
                                     (button :text "Manual play")
                                     gap-ho
                                     (button :text "Breed snakes"
                                             :listen [:action (fn [e] (-> breed-frame show!))])
                                     gap-ho
                                     (button :text " Run snake ")
                                     gap-ho
                                     (button :text "   About   ")
                                     gap-ho])])
   :south (horizontal-panel :items [gap-ho (label :icon (clojure.java.io/resource "../resources/snake.png")) gap-ho])))

(def main-frame
  (frame :title "Snake pit"
         :id "main"
         :content main-frame-content
         :height 200
         :width 500
         :resizable? false))

(-> main-frame show!)
