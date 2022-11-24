(ns mbezjak.core-extensions.number-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [mbezjak.core-extensions.number :as sut]))

(deftest constrain
  (is (= 1 (sut/constrain 1 9 1)))
  (is (= 3 (sut/constrain 1 9 3)))
  (is (= 9 (sut/constrain 1 9 9)))
  (is (= -5 (sut/constrain -9 -1 -5)))
  (is (= 0 (sut/constrain -9 9 0)))

  (is (= 1 (sut/constrain 1 9 -1)))
  (is (= 1 (sut/constrain 1 9 0)))
  (is (= 9 (sut/constrain 1 9 10))))

(deftest set-scale
  (testing "doubles"
    (is (= 0.0 (sut/set-scale 1 0.001)))
    (is (= 0.3 (sut/set-scale 1 0.345)))
    (is (= 0.5 (sut/set-scale 1 0.456)))
    (is (= 0.6 (sut/set-scale 1 0.567)))
    (is (= 0.57 (sut/set-scale 2 0.567)))
    (is (= 1.35 (sut/set-scale 7 1.35))))
  (testing "non doubles"
    (is (= 0.0 (sut/set-scale 1 0)))
    (is (= 7.0 (sut/set-scale 1 7)))
    (is (= 0.33 (sut/set-scale 2 (/ 1 3))))))

(defn- close-to? [x y]
  (<= (abs (- x y)) 1e-6))

(deftest convert-cm-and-ft
  (doseq [x [0 1 5 10 100 1e6 1e9 133.59 1/2 1/3 1/9 0.5123512]]
    (is (close-to? x (sut/ft->cm (sut/cm->ft x))))
    (is (close-to? x (sut/cm->ft (sut/ft->cm x))))))

(deftest convert-kg-and-lb
  (doseq [x [0 1 5 10 100 1e6 1e9 133.59 1/2 1/3 1/9 0.5123512]]
    (is (close-to? x (sut/lb->kg (sut/kg->lb x))))
    (is (close-to? x (sut/kg->lb (sut/lb->kg x))))))