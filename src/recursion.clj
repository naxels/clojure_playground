(ns recursion
  (:require [clojure.test :refer [is]]))

;; collection
(def test-coll [0 1 2 3 4])

;; call self

; https://practicalli.github.io/clojure/thinking-functionally/recursion.html

(defn length [coll]
  (if (empty? coll)
    0
    (+ 1 (length (rest coll)))))

(is (= 5 (length test-coll)))

(is (= 0 (length [])))

(defn my-map [func a-list]
  (when a-list
    (cons (func (first a-list))
          (my-map func (next a-list))))) ; normally use rest, not next

(is (= (list 1 2 3 4 5) (my-map #(+ 1 %) test-coll)))

;; recur
(defn length-recur
  ([coll] (length-recur coll 0))
  ([coll cnt]
   (if (empty? coll)
     cnt
     (recur (rest coll) (+ 1 cnt)))))

(is (= 5 (length-recur test-coll)))

(is (= 0 (length-recur [])))

; https://practicalli.github.io/clojure/thinking-functionally/tail-recursion.html

(defn sum
  ([coll] (sum coll 0))
  ([coll accum]
   (if (empty? coll)
     accum
     (recur (rest coll) (+ (first coll) accum)))))

(is (= 10 (sum test-coll)))

(is (= 0 (sum [])))

;; loop
(loop [x 10]
  (when (> x 1)
    (println x)
    (recur (- x 2))))

(loop [iter 1
       acc  0]
  (if (> iter 10)
    (println acc)
    (recur (inc iter) (+ acc iter))))

;; for
(for [coll [0 1 2 3 4 5]
      :let [y (* coll 3)]
      :when (even? y)]
  [coll y])

(for [x (range 6)
      :let [y (* x x)
            z (* x x x)]]
  [x y z])

(for [x (range 1 6)]
  (* x x))

;; combined
(defn fib [n]
  (loop [a 1 b 0 cnt n] ; inner loop so that function doesn't have to repeat and has easy interface
    (if (zero? cnt)
      b
      (recur (+' a b) a (dec cnt))))) ; +' allows for overflow of Integer

(map fib (range 100)) ; enters into overflow Integer

(is (= (list 0 1 1 2 3 5 8 13 21 34) (map fib (range 10))))

;; Elegance of Clojure for fib:
;; uses destruct, +' for big int, generates data vector
;; iterate to keep on generating a lazy seq based on the previous result
(def fibo
  (iterate
   (fn [[x y]] ; destruct into x and y
     [y (+' x y)]) ; create a vec with y value + (x + y), basically last result + new calc
   [0 1])) ; start vec

(take 10 (map first fibo)) ; first value, the one that matters to create the fib list
(take 10 (map second fibo))
(take 7 (map identity fibo)) ; shows the actual vector
;; ([0 1]
;;  [1 1]
;;  [1 2]
;;  [2 3]
;;  [3 5]
;;  [5 8]
;;  [8 13])

; defn has an explicit do form
; anonymous functions don't

(defn countdown
  [x]
  (if (zero? x)
    :blastoff!
    (do (println x)
        (recur (dec x)))))

(countdown 5)
