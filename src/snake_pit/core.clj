(ns snake-pit.core
  (:use fungp.core)
  (:use fungp.util)
  (:use clojure.pprint))

;;; Constants used in the game
(def MAX_STEPS "Maximum number of steps that the snake takes" 300)
(def WIDTH "Width of the game board" 19)
(def HEIGHT "Height of the game board" 10)
(def MAX_APPLES "Maximum number of apples that the snake can eat" 211)
(def TRIALS "Number of trials for each snake" 4)
(def LEFT "Left direction" [-1 0])
(def RIGHT "Right direction" [1 0])
(def UP "Up direction" [0 -1])
(def DOWN "Down direction" [0 1])
(def RIGHT-TURN "Right turn vector" [1 -1])
(def LEFT-TURN "Left turn vector" [-1 1])

;;; State variables
(def ^:dynamic snake)
(def ^:dynamic apple)
(def ^:dynamic direction)
(def ^:dynamic steps)

(defn add-points [& pts]
  "Add vector points"
  (vec (apply map + pts)))

(defn eats? [{[snake-head] :body} {apple :location}]
  "Check if the snake eats an apple"
  (= snake-head apple))

(defn create-apple []
  "Create an apple"
  {:location [(rand-int WIDTH) (rand-int HEIGHT)]
   :type :apple})

(defn create-snake []
  "Create the snake"
  {:body (for [x (range 8 -1 -1)] [x 10])
   :type :snake})

(defn change-direction [old-dir turn]
  "Change direction of the snake"
  (vec (reverse (map * old-dir turn))))

(defn move [{:keys [body] :as snake} dir]
  "Move the snake in a given direction."
  (assoc snake :body (cons (add-points (first body) dir)
                           (if (eats? snake apple) body (butlast body)))))

(defn turn-right []
  "Make the snake turn right."
  (set! direction (change-direction direction RIGHT-TURN))
  (set! snake (move snake direction)))

(defn turn-left []
  "Make the snake turn left."
  (set! direction (change-direction direction LEFT-TURN))
  (set! snake (move snake direction)))

(defn move-forward []
  "Make to snake continue forward."
  (set! snake (move snake direction)))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
