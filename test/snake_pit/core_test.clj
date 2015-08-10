(ns snake-pit.core-test
  (:require [clojure.test :refer :all]
            [snake-pit.core :refer :all]))

(deftest test-add-points
  (testing "Testing points addition."
    (is (= [9 12] (add-points [1 2] [3 4] [5 6])))
    (is (not= [10 11] (add-points [1 2] [3 4] [5 6])))))

(deftest test-eats
  (testing "Testing if the snake reached an apple."
  (let [snake {:body '([1 0] [1 1] [1 2])}
        apple-1 {:location [1 0]}
        apple-2 {:location [1 1]}]
    (is (= true (eats? snake apple-1)))
    (is (= false (eats? snake apple-2))))))

(deftest test-out-of-bounds
  (testing "Testing if the snake is out of bounds."
    (let [snake-out-l {:body '([-1 5] [0 5] [1 5])}
          snake-out-r {:body '([20 5] [19 5] [18 5])}
          snake-out-u {:body '([10 -1] [10 0] [10 1])}
          snake-out-d {:body '([10 11] [10 10] [10 9])}
          snake-in-1 {:body '([0 0])}
          snake-in-2 {:body '([19 0])}
          snake-in-3 {:body '([0 10])}
          snake-in-4 {:body '([19 10])}]
      (are [snake target] (= target (out-of-bounds? snake))
           snake-out-l true
           snake-out-r true
           snake-out-u true
           snake-out-d true
           snake-in-1 false
           snake-in-2 false
           snake-in-3 false
           snake-in-4 false))))

(deftest test-head-overlaps-body
  (testing "Testing if the snake has collided with itself."
    (let [snake-1 {:body '([10 5] [10 4] [10 3] [10 2] [10 1])}
          snake-2 {:body '([10 4][11 4] [12 4] [12 5] [11 5] [10 5] [10 4] [10 3] [10 2] [10 1])}]
      (are [snake target] (= target (head-overlaps-body? snake))
           snake-1 false
           snake-2 true))))

(deftest test-change-direction
  (testing "Testing the snake direction change"
    (is (= LEFT (change-direction UP LEFT-TURN)))
    (is (= LEFT (change-direction DOWN RIGHT-TURN)))
    (is (= RIGHT (change-direction UP RIGHT-TURN)))
    (is (= RIGHT (change-direction DOWN LEFT-TURN)))
    (is (= UP (change-direction LEFT RIGHT-TURN)))
    (is (= UP (change-direction RIGHT LEFT-TURN)))
    (is (= DOWN (change-direction LEFT LEFT-TURN)))
    (is (= DOWN (change-direction RIGHT RIGHT-TURN)))))

(deftest test-move-no-grow
  (testing "Testing the snake position change without grow."

    (let [right-snake {:body '([8 5] [7 5] [6 5] [5 5] [4 5]) :score 0}
          right-snake-forward {:body '([9 5] [8 5] [7 5] [6 5] [5 5]) :score 0}
          right-snake-up {:body '([8 4] [8 5] [7 5] [6 5] [5 5]) :score 0}
          right-snake-down {:body '([8 6] [8 5] [7 5] [6 5] [5 5]) :score 0}

          left-snake {:body '([8 5] [9 5] [10 5] [11 5] [12 5]) :score 0}
          left-snake-forward {:body '([7 5] [8 5] [9 5] [10 5] [11 5]) :score 0}
          left-snake-up {:body '([8 4] [8 5] [9 5] [10 5] [11 5]) :score 0}
          left-snake-down {:body '([8 6] [8 5] [9 5] [10 5] [11 5]) :score 0}

          up-snake {:body '([8 5] [8 6] [8 7] [8 8] [8 9]) :score 0}
          up-snake-forward {:body '([8 4] [8 5] [8 6] [8 7] [8 8]) :score 0}
          up-snake-left {:body '([7 5] [8 5] [8 6] [8 7] [8 8]) :score 0}
          up-snake-right {:body '([9 5] [8 5] [8 6] [8 7] [8 8]) :score 0}

          down-snake {:body '([8 5] [8 4] [8 3] [8 2] [8 1]) :score 0}
          down-snake-forward {:body '([8 6] [8 5] [8 4] [8 3] [8 2]) :score 0}
          down-snake-left {:body '([9 5] [8 5] [8 4] [8 3] [8 2]) :score 0}
          down-snake-right {:body '([7 5] [8 5] [8 4] [8 3] [8 2]) :score 0}

          apple {:location [15 4]}]

      (are [target-snake snake dir] (= target-snake (move snake dir apple))

            right-snake-forward right-snake RIGHT
            right-snake-up right-snake UP
            right-snake-down right-snake DOWN

            left-snake-forward left-snake LEFT
            left-snake-up left-snake UP
            left-snake-down left-snake DOWN

            up-snake-forward up-snake UP
            up-snake-left up-snake LEFT
            up-snake-right up-snake RIGHT

            down-snake-forward down-snake DOWN
            down-snake-left down-snake RIGHT
            down-snake-right down-snake LEFT))))

(deftest test-move-grow
  (testing "Testing the snake position change with grow."
    (binding [apple (create-apple)
              score 0
              steps 0]
     (let [right-snake {:body '([8 5] [7 5] [6 5] [5 5] [4 5]) :score 0}
          right-snake-forward {:body '([9 5] [8 5] [7 5] [6 5] [5 5] [4 5]) :score 0}
          right-snake-up {:body '([8 4] [8 5] [7 5] [6 5] [5 5] [4 5]) :score 0}
          right-snake-down {:body '([8 6] [8 5] [7 5] [6 5] [5 5] [4 5]) :score 0}

          left-snake {:body '([8 5] [9 5] [10 5] [11 5] [12 5]) :score 0}
          left-snake-forward {:body '([7 5] [8 5] [9 5] [10 5] [11 5] [12 5]) :score 0}
          left-snake-up {:body '([8 4] [8 5] [9 5] [10 5] [11 5] [12 5]) :score 0}
          left-snake-down {:body '([8 6] [8 5] [9 5] [10 5] [11 5] [12 5]) :score 0}

          up-snake {:body '([8 5] [8 6] [8 7] [8 8] [8 9]) :score 0}
          up-snake-forward {:body '([8 4] [8 5] [8 6] [8 7] [8 8] [8 9]) :score 0}
          up-snake-left {:body '([7 5] [8 5] [8 6] [8 7] [8 8] [8 9]) :score 0}
          up-snake-right {:body '([9 5] [8 5] [8 6] [8 7] [8 8] [8 9]) :score 0}

          down-snake {:body '([8 5] [8 4] [8 3] [8 2] [8 1]) :score 0}
          down-snake-forward {:body '([8 6] [8 5] [8 4] [8 3] [8 2] [8 1]) :score 0}
          down-snake-left {:body '([9 5] [8 5] [8 4] [8 3] [8 2] [8 1]) :score 0}
          down-snake-right {:body '([7 5] [8 5] [8 4] [8 3] [8 2] [8 1]) :score 0}]

      (are [target-snake snake dir] (= target-snake (move snake dir {:location (first (:body snake))}))

            right-snake-forward right-snake RIGHT
            right-snake-up right-snake UP
            right-snake-down right-snake DOWN

            left-snake-forward left-snake LEFT
            left-snake-up left-snake UP
            left-snake-down left-snake DOWN

            up-snake-forward up-snake UP
            up-snake-left up-snake LEFT
            up-snake-right up-snake RIGHT

            down-snake-forward down-snake DOWN
            down-snake-left down-snake RIGHT
            down-snake-right down-snake LEFT)))))

(deftest test-food-ahead
  (testing "Testing food detection (food ahead)."
    (let [snake {:body '([9 5])}
          apple-left {:location [2 5]}
          apple-right {:location [17 5]}
          apple-up {:location [9 1]}
          apple-down {:location [9 9]}]
      (are [dir apple target] (= target (food-ahead? snake dir apple))
           LEFT apple-left true
           RIGHT apple-right true
           UP apple-up true
           DOWN apple-down true

           LEFT apple-up false
           LEFT apple-down false
           LEFT apple-right false

           RIGHT apple-up false
           RIGHT apple-down false
           RIGHT apple-left false

           UP apple-left false
           UP apple-right false
           UP apple-down false

           DOWN apple-left false
           DOWN apple-right false
           DOWN apple-up false))))

(deftest test-danger-ahead
  (testing "Testing danger detection (danger ahead)."
    (let [snake-ovl {:body '([9 5] [10 5] [8 5] [9 4] [9 6])}
          snake-ok {:body '([9 5] [11 5] [7 5] [9 3] [9 7])}
          snake-top-corner {:body '([0 0])}
          snake-bottom-corner {:body '([19 10])}]
      (are [snake dir target] (= target (danger-ahead? snake dir))
           snake-ovl LEFT true
           snake-ovl RIGHT true
           snake-ovl UP true
           snake-ovl DOWN true
           snake-ok LEFT false
           snake-ok RIGHT false
           snake-ok UP false
           snake-ok DOWN false
           snake-top-corner LEFT true
           snake-top-corner RIGHT false
           snake-top-corner UP true
           snake-top-corner DOWN false
           snake-bottom-corner LEFT false
           snake-bottom-corner RIGHT true
           snake-bottom-corner UP false
           snake-bottom-corner DOWN true))))

(deftest test-danger-right
  (testing "Testing danger detection (danger right)."
    (let [snake-ovl {:body '([9 5] [10 5] [8 5] [9 4] [9 6])}
          snake-ok {:body '([9 5] [11 5] [7 5] [9 3] [9 7])}
          snake-top-corner {:body '([0 0])}
          snake-bottom-corner {:body '([19 10])}]
      (are [snake dir target] (= target (danger-right? snake dir))
           snake-ovl LEFT true
           snake-ovl RIGHT true
           snake-ovl UP true
           snake-ovl DOWN true
           snake-ok LEFT false
           snake-ok RIGHT false
           snake-ok UP false
           snake-ok DOWN false
           snake-top-corner LEFT true
           snake-top-corner RIGHT false
           snake-top-corner UP false
           snake-top-corner DOWN true
           snake-bottom-corner LEFT false
           snake-bottom-corner RIGHT true
           snake-bottom-corner UP true
           snake-bottom-corner DOWN false))))

(deftest test-danger-left
  (testing "Testing danger detection (danger left)."
    (let [snake-ovl {:body '([9 5] [10 5] [8 5] [9 4] [9 6])}
          snake-ok {:body '([9 5] [11 5] [7 5] [9 3] [9 7])}
          snake-top-corner {:body '([0 0])}
          snake-bottom-corner {:body '([19 10])}]
      (are [snake dir target] (= target (danger-left? snake dir))
           snake-ovl LEFT true
           snake-ovl RIGHT true
           snake-ovl UP true
           snake-ovl DOWN true
           snake-ok LEFT false
           snake-ok RIGHT false
           snake-ok UP false
           snake-ok DOWN false
           snake-top-corner LEFT false
           snake-top-corner RIGHT true
           snake-top-corner UP true
           snake-top-corner DOWN false
           snake-bottom-corner LEFT true
           snake-bottom-corner RIGHT false
           snake-bottom-corner UP false
           snake-bottom-corner DOWN true))))

(deftest test-danger-two-ahead
  (testing "Testing danger detection (danger two ahead)."
    (let [snake-ovl {:body '([9 5] [11 5] [7 5] [9 3] [9 7])}
          snake-ok {:body '([9 5] [12 5] [6 5] [9 2] [9 8])}
          snake-top-corner {:body '([1 1])}
          snake-bottom-corner {:body '([18 9])}]
      (are [snake dir target] (= target (danger-two-ahead? snake dir))
           snake-ovl LEFT true
           snake-ovl RIGHT true
           snake-ovl UP true
           snake-ovl DOWN true
           snake-ok LEFT false
           snake-ok RIGHT false
           snake-ok UP false
           snake-ok DOWN false
           snake-top-corner LEFT true
           snake-top-corner RIGHT false
           snake-top-corner UP true
           snake-top-corner DOWN false
           snake-bottom-corner LEFT false
           snake-bottom-corner RIGHT true
           snake-bottom-corner UP false
           snake-bottom-corner DOWN true))))

(deftest test-food-up
  (testing "Testing food above snake."
    (let [snake-1 {:body '([11 7] [10 7] [9 7] [9 6] [9 5] [9 4] [9 3] [9 2] [9 1])}
          snake-2 {:body '([11 3] [10 3] [9 3] [9 4] [9 5] [9 6] [9 7] [9 8] [9 9])}
          apple-1 {:location [12 2]}
          apple-2 {:location [12 9]}
          apple-3 {:location [18 7]}]
      (are [snake apple target] (= target (food-up? snake apple))
           snake-1 apple-1 true
           snake-1 apple-2 false
           snake-1 apple-3 false
           snake-2 apple-1 true
           snake-2 apple-2 false
           snake-2 apple-3 false))))

(deftest test-food-right
  (testing "Testing food further right."
    (let [snake-1 {:body '([15 7] [14 7] [13 7] [13 6] [14 6] [15 6] [16 6] [17 6] [18 6])}
          snake-2 {:body '([5 5] [6 5] [7 5] [7 6] [6 6] [5 6] [4 6] [3 6] [2 6])}
          apple-1 {:location [18 2]}
          apple-2 {:location [2 4]}
          apple-3 {:location [15 1]}]
      (are [snake apple target] (= target (food-right? snake apple))
           snake-1 apple-1 true
           snake-1 apple-2 false
           snake-1 apple-3 false
           snake-2 apple-1 true
           snake-2 apple-2 false
           snake-2 apple-3 true))))

(deftest test-moving-right
  (testing "Testing snake right movement."
    (are [dir target] (= target (moving-right? dir))
         RIGHT true
         LEFT false
         UP false
         DOWN false)))

(deftest test-moving-left
  (testing "Testing snake left movement."
    (are [dir target] (= target (moving-left? dir))
         RIGHT false
         LEFT true
         UP false
         DOWN false)))

(deftest test-moving-up
  (testing "Testing snake up movement."
    (are [dir target] (= target (moving-up? dir))
         RIGHT false
         LEFT false
         UP true
         DOWN false)))

(deftest test-moving-down
  (testing "Testing snake down movement."
    (are [dir target] (= target (moving-down? dir))
         RIGHT false
         LEFT false
         UP false
         DOWN true)))

(deftest test-distance-from-food
  (testing "Testing distance from food."
    (let [snake {:body '([9 5])}
          apple-0 {:location [8 5]}
          apple-1 {:location [18 5]}
          apple-2 {:location [9 1]}
          apple-3 {:location [18 1]}
          apple-4 {:location [1 9]}]
      (are [snake apple target] (= target (distance-from-food snake apple))
           snake apple-0 1
           snake apple-1 9
           snake apple-2 4
           snake apple-3 13
           snake apple-4 12))))




