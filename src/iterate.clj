(ns iterate
  (:require [clojure.test :refer [is]]))

; https://mrhaki.blogspot.com/2020/04/clojure-goodness-reapply-functions-with.html

;; Lazy sequence of numbers in steps of 2.
(def odds (iterate #(+ 2 %) 1))

(is (= (list 1 3 5 7 9 11 13 15 17 19)
       (take 10 odds)))


;; Define lazy sequence with a growing string.
;; The first element is ar, next argh, then arghgh etc.
(def pirate (iterate #(str % "gh") "ar"))

(def crumpy-pirate (nth pirate 5))

(is (= "arghghghghgh" crumpy-pirate))


;; Function that returns the given amount
;; plus interest of 1.25%.
(defn cumulative-interest
  [amount]
  (+ amount (* 0.0125 amount)))

;; Lazy sequence where each entry is the
;; cumulative amount with interest based
;; on the previous entry.
;; We start our savings at 500.
(def savings (iterate cumulative-interest 500))

;; After 5 years we have:
(is (= 532.0410768127441
       (nth savings 5)))


;; Function to double a given integer
;; and return as bigint.
(defn doubler [n] (bigint (+ n n)))

;; Define lazy infinitive sequence
;; where each element is the doubled value
;; of the previous element.
(def wheat-chessboard (iterate doubler 1))

;; First elements are still small.
(is (= (list 1 2 4 8 16 32)
       (take 6 wheat-chessboard)))

;; Later the elements grow much bigger.
(is (= (list 4611686018427387904N 9223372036854775808N)
       (->> wheat-chessboard (drop 62) (take 2))))

;; Sum of all values for all chessboard squares
;; is an impressive number.
(is (= 18446744073709551615N
       (reduce + (take 64 wheat-chessboard))))