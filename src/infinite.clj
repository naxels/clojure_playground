(ns infinite
  (:require [clojure.test :refer [is]]
            data))

;; cycle
; will cycle through the collection infinitely
(defn give_candy
  [candy-coll to-coll]
  (zipmap to-coll (cycle candy-coll))) ; will stop when any of the colls finishes

(is (= {"Mike" "chocolate"
        "Anna" "jelly"
        "Ted" "mint"
        "Mary" "chocolate"
        "Alex" "jelly"
        "Emma" "mint"
        "Piet" "chocolate"}
       (give_candy data/candy data/kids)))

;; repeat - lazy infinite (or n) sequence
(take 5 (repeat "x"))
; same as
(repeat 5 "x")

;; repeatedly - lazy infinitely (or n) call a function used for side effects like randoms
(take 10 (repeatedly #(rand-int 11)))
; same as
(repeatedly 10 #(rand-int 100))

;; iterate
(take 5 (iterate inc 5))

; generates a list of what happens when you get 5% interest on a principle of $1000
(take 16 (iterate #(* % 1.05) 1000)) ; doubles in 15 years

(nth (iterate #(* % 1.05) 1000) 15) ; just get the nth value

;; range
(take 10 (range))

; this shows the lazyness of all the intermediate functions
(->> (range) ; if you change it to a finite range (range 0 5) it will not crash, however return less than 10 results
     (filter even?)
     (map #(* 3 %))
     (map str)
     (take 10))

; finite range
(range -5 5)

; with steps of 10
(range -100 100 10)

;; lazy-seq
(defn positive-numbers
  ([] (positive-numbers 1))
  ([n]
   (lazy-seq (cons n (positive-numbers (inc n))))))

(take 50 (positive-numbers))

(take 50 (positive-numbers 10))