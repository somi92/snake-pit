(ns snake-pit.core
  (:use fungp.core)
  (:use fungp.util)
  (:use clojure.pprint))

;;;
;;; Constants used in the game
;;;
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
(def CRITERIA "Acceptable minimum value." 843)

;;;
;;; State variables
;;;
(def ^:dynamic snake)
(def ^:dynamic apple)
(def ^:dynamic direction)
(def ^:dynamic steps)
(def ^:dynamic score)

;;;
;;; Util functions
;;;
(defn add-points
  "Add vector points."
  [& pts]
  (vec (apply map + pts)))

(defn eats?
  "Check if the snake eats an apple."
  [{[snake-head] :body} {apple :location}]
  (= snake-head apple))

(defn out-of-bounds?
  "Check if the snake is out of bounds (wall hit)."
  [{[head] :body}]
  (or (< (head 0) 0)
      (> (head 0) WIDTH)
      (< (head 1) 0)
      (> (head 1) HEIGHT)))

(defn head-overlaps-body?
  "Check if the snake has collided with itself."
  [{[head & body] :body}]
  (contains? (set body) head))

(defn lose? [snake]
  (or (head-overlaps-body? snake) (out-of-bounds? snake)))

(defn create-apple
  "Create an apple."
  []
  {:location [(rand-int WIDTH) (rand-int HEIGHT)]
   :type :apple})

(defn create-snake
  "Create the snake."
  []
  {:body (for [x (range 8 -1 -1)] [x 10])
   :score 0
   :type :snake})

(defn change-direction
  "Change direction of the snake."
  [old-dir turn]
  (vec (reverse (map * old-dir turn))))

(defn move
  "Move the snake in a given direction."
  [{:keys [body] :as snake} dir apple-loc]
  (assoc snake :body (cons (add-points (first body) dir)
                           (if (eats? snake apple-loc)
                             (do
                               (set! apple (create-apple))
                               (set! score (inc score))
                               (set! steps 0)
                               body)
                             (butlast body)))))

(defn food-ahead?
  "Check if an apple is in line with the snake's current direction."
  [{[head] :body} dir {apple :location}]
  (let [x-head (first head) y-head (last head)
        x-apple (first apple) y-apple (last apple)]
    (cond
     (= dir UP) (if (and (= x-head x-apple) (> y-head y-apple)) true false)
     (= dir DOWN) (if (and (= x-head x-apple) (< y-head y-apple)) true false)
     (= dir LEFT) (if (and (= y-head y-apple) (> x-head x-apple)) true false)
     (= dir RIGHT) (if (and (= y-head y-apple) (< x-head x-apple)) true false))))

(defn danger-ahead?
  "Check if position ahead of current snake's direction is occupied by a wall or snake segment."
  [{[head] :body :as snake} dir]
  (let [next-pos (add-points head dir)]
    (if (or (out-of-bounds? {:body (list next-pos)}) (head-overlaps-body? {:body (conj (:body snake) next-pos)}))
      true
      false)))

(defn danger-right?
  "Check if position to the right of current snake's direction is occupied by a wall or snake segment."
  [{[head] :body :as snake} dir]
  (let [next-pos (add-points head (change-direction dir RIGHT-TURN))]
    (if (or (out-of-bounds? {:body (list next-pos)}) (head-overlaps-body? {:body (conj (:body snake) next-pos)}))
      true
      false)))

(defn danger-left?
  "Check if position to the left of current snake's direction is occupied by a wall or snake segment."
  [{[head] :body :as snake} dir]
  (let [next-pos (add-points head (change-direction dir LEFT-TURN))]
    (if (or (out-of-bounds? {:body (list next-pos)}) (head-overlaps-body? {:body (conj (:body snake) next-pos)}))
      true
      false)))

(defn danger-two-ahead?
  "Check if position two steps ahead of current snake's direction is occupied by a wall or snake segment."
  [{[head] :body :as snake} dir]
  (let [next-pos (add-points (add-points head dir) dir)]
    (if (or (out-of-bounds? {:body (list next-pos)}) (head-overlaps-body? {:body (conj (:body snake) next-pos)}))
      true
      false)))

(defn food-up?
  "Check if the current apple on the board is closer to the top of the board than the snake's head."
  [{[head] :body} {apple :location}]
  (let [head-y (head 1)
        apple-y (apple 1)]
    (if (> head-y apple-y)
      true
      false)))

(defn food-right?
  "Check if the current apple on the board is further to the right of the board than the snake's head."
  [{[head] :body} {apple :location}]
  (let [head-x (head 0)
        apple-x (apple 0)]
    (if (< head-x apple-x)
      true
      false)))

(defn moving-right?
  "Check if the snake's current direction is right."
  [dir]
  (if (= dir RIGHT)
    true
    false))

(defn moving-left?
  "Check if the snake's current direction is left."
  [dir]
  (if (= dir LEFT)
    true
    false))

(defn moving-up?
  "Check if the snake's current direction is up."
  [dir]
  (if (= dir UP)
    true
    false))

(defn moving-down?
  "Check if the snake's current direction is down."
  [dir]
  (if (= dir DOWN)
    true
    false))

(defn distance-from-food
  "Calculate the snake's distance from food."
  [{[head] :body} {apple :location}]
  (let [head-x (head 0) head-y (head 1)
        apple-x (apple 0) apple-y (apple 1)]
    (+ (Math/abs (- head-x apple-x)) (Math/abs (- head-y apple-y)))))

;;;
;;; GP terminals
;;;
(defn turn-right
  "Make the snake turn right."
  []
  (set! direction (change-direction direction RIGHT-TURN))
  (set! snake (move snake direction apple))
  (set! steps (inc steps)))

(defn turn-left
  "Make the snake turn left."
  []
  (set! direction (change-direction direction LEFT-TURN))
  (set! snake (move snake direction apple))
  (set! steps (inc steps)))

(defn move-forward
  "Make to snake continue forward."
  []
  (set! snake (move snake direction apple))
  (set! steps (inc steps)))

;;;
;;; GP functions
;;;
;;; (initial function set)
;;;
(defmacro if-food-ahead
  "GP food ahead macro."
  [food no-food]
  `(if (food-ahead? snake direction apple)
     ~food
     ~no-food))

(defmacro if-danger-ahead
  "GP danger ahead macro."
  [danger no-danger]
  `(if (danger-ahead? snake direction)
     ~danger
     ~no-danger))

(defmacro if-danger-right
  "GP danger right macro."
  [danger-right no-danger-right]
  `(if (danger-right? snake direction)
     ~danger-right
     ~no-danger-right))

(defmacro if-danger-left
  "GP danger left macro."
  [danger-left no-danger-left]
  `(if (danger-left? snake direction)
     ~danger-left
     ~no-danger-left))

;;; function (do exprs*) is used as progn2

;;;
;;; GP functions
;;;
;;; (full function set)
;;;
(defmacro if-danger-two-ahead
  "GP danger two ahead macro."
  [danger-two-ahead no-danger-two-ahead]
  `(if (danger-two-ahead? snake direction)
     ~danger-two-ahead
     ~no-danger-two-ahead))

(defmacro if-food-up
  "GP food up macro."
  [food-up no-food-up]
  `(if (food-up? snake apple)
     ~food-up
     ~no-food-up))

(defmacro if-food-right
  "GP food right macro."
  [food-right no-food-right]
  `(if (food-right? snake apple)
     ~food-right
     ~no-food-right))

(defmacro if-moving-right
  "GP moving right macro."
  [moving-right no-moving-right]
  `(if (moving-right? direction)
     ~moving-right
     ~no-moving-right))

(defmacro if-moving-left
  "GP moving left macro."
  [moving-left no-moving-left]
  `(if (moving-left? direction)
     ~moving-left
     ~no-moving-left))

(defmacro if-moving-up
  "GP moving up macro."
  [moving-up no-moving-up]
  `(if (moving-up? direction)
     ~moving-up
     ~no-moving-up))

(defmacro if-moving-down
  "GP moving down macro."
  [moving-down no-moving-down]
  `(if (moving-down? direction)
     ~moving-down
     ~no-moving-down))

;;;
;;; GP setup
;;;
(def snake-terminals '[(turn-left) (turn-right) (move-forward)])
(def snake-functions '[[if-food-ahead 2] [if-danger-ahead 2] [if-danger-right 2] [if-danger-left 2] [do 2]
                      [if-danger-two-ahead 2] [if-food-up 2] [if-food-right 2] [if-moving-right 2] [if-moving-left 2]
                       [if-moving-up 2] [if-moving-down 2]])

;(def snake-functions '[[if-food-ahead 2] [if-danger-ahead 2] [if-danger-right 2] [if-danger-left 2] [do 2]])

(defn simulate-snake
  "This function repeatedly runs the evolved individual and checks
  the dynamic variables to see if the snake has run out of time or died."
  [f]
  (binding [snake (create-snake), apple (create-apple),
            direction RIGHT, steps 0, score 0]
           (loop []
                 (f)
                 (cond
                  (or (> steps MAX_STEPS) (lose? snake))
                  (if (= score 0) (+ MAX_APPLES (distance-from-food snake apple)) (- MAX_APPLES score))
                  :else (recur)))))

(defn snake-error
  "Calculate the error of the evolved program."
  [tree]
  (let [f (eval (list 'fn [] tree))]
    (apply + (repeatedly TRIALS #(simulate-snake f)))))

(defn snake-fitness
  "Calculate the fitness, taking the criteria into account."
  [tree]
  (let [error (snake-error tree)]
    (if (> error CRITERIA) error 0)))

(defn snake-report
  [tree fitness]
  (pprint (nth tree 2))
  (println (str "Error:\t" fitness "\n\n")))

(defn run-snakes-gp [gp-options]
  (println "Snake game")
  (let [options {:iterations (:iterations gp-options) :migrations (:migrations gp-options)
                 :num-islands (:num-islands gp-options) :tournament-size (:tournament-size gp-options)
                 :population-size (:population-size gp-options) :max-depth (:max-depth gp-options)
                 :terminals snake-terminals :fitness snake-fitness
                 :functions snake-functions :report snake-report :mutation-probability (:mutation-probability gp-options)
        }
        [tree score] (rest (run-genetic-programming options))]
    (do (println "Done!")
        (snake-report tree score))))






