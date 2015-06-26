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

(deftest test-move
  (testing "Testing the snake position change"
    ))
