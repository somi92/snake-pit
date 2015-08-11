(ns snake-pit.gui.breed
  (:use seesaw.core)
  (:use snake-pit.core))

(def gap-h [:fill-h 20])
(def gap-v [:fill-v 5])
(def label-size [140 :by 25])
(def param-text-size [200 :by 25])

(def params-panel (border-panel
            :center (vertical-panel
                     :items [gap-v
                             (horizontal-panel :items [gap-h (label :text "Migrations: "
                                                                    :size label-size) gap-h
                                                       (text :id :migrations
                                                             :text "5"
                                                             :halign :center
                                                             :size param-text-size) gap-h])
                             gap-v
                             (horizontal-panel :items [gap-h (label :text "Iterations: "
                                                                    :size label-size) gap-h
                                                       (text :id :iterations
                                                             :text "5"
                                                             :halign :center
                                                             :size param-text-size) gap-h])
                             gap-v
                             (horizontal-panel :items [gap-h (label :text "Number of islands: "
                                                                    :size label-size) gap-h
                                                       (text :id :islands
                                                             :text "4"
                                                             :halign :center
                                                             :size param-text-size) gap-h])
                             gap-v
                             (horizontal-panel :items [gap-h (label :text "Tournament size: "
                                                                    :size label-size) gap-h
                                                       (text :id :tournament
                                                             :text "7"
                                                             :halign :center
                                                             :size param-text-size) gap-h])
                             gap-v
                             (horizontal-panel :items [gap-h (label :text "Population size: "
                                                                    :size label-size) gap-h
                                                       (text :id :population
                                                             :text "1000"
                                                             :halign :center
                                                             :size param-text-size) gap-h])
                             gap-v
                             (horizontal-panel :items [gap-h (label :text "Maximum depth: "
                                                                    :size label-size) gap-h
                                                       (text :id :depth
                                                             :text "5"
                                                             :halign :center
                                                             :size param-text-size) gap-h])
                             gap-v
                             (horizontal-panel :items [gap-h (label :text "Mutation rate: "
                                                                    :size label-size) gap-h
                                                       (text :id :mutation
                                                             :text "0.3"
                                                             :halign :center
                                                             :size param-text-size) gap-h])])))

(defn run-gp
  []
  ())


(def result-panel (border-panel
                   :center (vertical-panel
                            :items [(vertical-panel :items [(horizontal-panel :items [gap-h (label :text "Results: "
                                                                                                   :size label-size) gap-h
                                                                                            (button :text "Run GP") gap-h
                                                                                            (button :text "Stop GP")])
                                                            gap-v
                                                            (scrollable (text :id :result-area
                                                                              :multi-line? true
                                                                              :editable? false)
                                                                              :size [380 :by 300])
                                                            gap-v
                                                            (horizontal-panel :items [(label :text "GP is running...        "
                                                                                             :visible? false)
                                                                                   (button :text "Save results to a file"
                                                                                           :visible? false)])])
                                                            gap-v])))

(def panel (vertical-panel :items [params-panel result-panel]))

(def breed-frame (frame
                  :title "Breed snakes"
                  :content panel
                  :height 620
                  :width 420
                  :resizable? false))

(-> breed-frame show!)

