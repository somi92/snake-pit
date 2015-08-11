(ns snake-pit.run
  (:use snake-pit.core))

(def gp-options
  {:iterations 1 :migrations 1 :num-islands 4
   :tournament-size 5 :population-size 500 :max-depth 3
   :mutation-probability 0.3})

(run-snakes-gp gp-options)
