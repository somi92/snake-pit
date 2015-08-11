(ns snake-pit.gui.main
  (:use seesaw.core))

(def gap-h [:fill-h 5])
(def gap-v [:fill-v 2])

(def main-frame-content
  (border-panel
   :north (vertical-panel
           :items [gap-v (horizontal-panel :items
                                           [gap-h (label "Application of genetic programming to a snake game.") gap-h])])
   :center (vertical-panel :items [gap-v (horizontal-panel :items [gap-h
                                     (button :text "Manual play")
                                     gap-h
                                     (button :text "Breed snakes")
                                     gap-h
                                     (button :text " Run snake ")
                                     gap-h
                                     (button :text "   About   ")
                                     gap-h])])
   :south (horizontal-panel :items [gap-h (label :icon (clojure.java.io/resource "../resources/snake.png")) gap-h])))

(def main-frame
  (frame :title "Snake pit"
         :id "main"
         :content main-frame-content
         :height 200
         :width 500
         :resizable? false))

(-> main-frame show!)
