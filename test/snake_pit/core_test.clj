(ns snake-pit.core-test
  (:require [clojure.test :refer :all]
            [snake-pit.core :refer :all]))

(deftest test-add-points
  (testing "Testing points addition."
    (is (= [9 12] (add-points [1 2] [3 4] [5 6])))
    (is (not= [10 11] (add-points [1 2] [3 4] [5 6])))))

(deftest test-eats
  (testing "Testing if the snake reached an apple"
  (let [snake {:body '([1 0] [1 1] [1 2])}
        apple-1 {:location [1 0]}
        apple-2 {:location [1 1]}]
    (is (= true (eats? snake apple-1)))
    (is (= false (eats? snake apple-2))))))

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
  (testing "Testing the snake position change without grow"

    (let [right-snake {:body '([8 5] [7 5] [6 5] [5 5] [4 5])}
          right-snake-forward {:body '([9 5] [8 5] [7 5] [6 5] [5 5])}
          right-snake-up {:body '([8 4] [8 5] [7 5] [6 5] [5 5])}
          right-snake-down {:body '([8 6] [8 5] [7 5] [6 5] [5 5])}

          left-snake {:body '([8 5] [9 5] [10 5] [11 5] [12 5])}
          left-snake-forward {:body '([7 5] [8 5] [9 5] [10 5] [11 5])}
          left-snake-up {:body '([8 4] [8 5] [9 5] [10 5] [11 5])}
          left-snake-down {:body '([8 6] [8 5] [9 5] [10 5] [11 5])}

          up-snake {:body '([8 5] [8 6] [8 7] [8 8] [8 9])}
          up-snake-forward {:body '([8 4] [8 5] [8 6] [8 7] [8 8])}
          up-snake-left {:body '([7 5] [8 5] [8 6] [8 7] [8 8])}
          up-snake-right {:body '([9 5] [8 5] [8 6] [8 7] [8 8])}

          down-snake {:body '([8 5] [8 4] [8 3] [8 2] [8 1])}
          down-snake-forward {:body '([8 6] [8 5] [8 4] [8 3] [8 2])}
          down-snake-left {:body '([9 5] [8 5] [8 4] [8 3] [8 2])}
          down-snake-right {:body '([7 5] [8 5] [8 4] [8 3] [8 2])}

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
  (testing "Testing the snake position change with grow"
    (let [right-snake {:body '([8 5] [7 5] [6 5] [5 5] [4 5])}
          right-snake-forward {:body '([9 5] [8 5] [7 5] [6 5] [5 5] [4 5])}
          right-snake-up {:body '([8 4] [8 5] [7 5] [6 5] [5 5] [4 5])}
          right-snake-down {:body '([8 6] [8 5] [7 5] [6 5] [5 5] [4 5])}

          left-snake {:body '([8 5] [9 5] [10 5] [11 5] [12 5])}
          left-snake-forward {:body '([7 5] [8 5] [9 5] [10 5] [11 5] [12 5])}
          left-snake-up {:body '([8 4] [8 5] [9 5] [10 5] [11 5] [12 5])}
          left-snake-down {:body '([8 6] [8 5] [9 5] [10 5] [11 5] [12 5])}

          up-snake {:body '([8 5] [8 6] [8 7] [8 8] [8 9])}
          up-snake-forward {:body '([8 4] [8 5] [8 6] [8 7] [8 8] [8 9])}
          up-snake-left {:body '([7 5] [8 5] [8 6] [8 7] [8 8] [8 9])}
          up-snake-right {:body '([9 5] [8 5] [8 6] [8 7] [8 8] [8 9])}

          down-snake {:body '([8 5] [8 4] [8 3] [8 2] [8 1])}
          down-snake-forward {:body '([8 6] [8 5] [8 4] [8 3] [8 2] [8 1])}
          down-snake-left {:body '([9 5] [8 5] [8 4] [8 3] [8 2] [8 1])}
          down-snake-right {:body '([7 5] [8 5] [8 4] [8 3] [8 2] [8 1])}]

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
            down-snake-right down-snake LEFT))))

(deftest test-food-ahead
  (testing "Testing food detection (food ahead)"
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











