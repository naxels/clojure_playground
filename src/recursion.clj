(ns recursion
  (:require [clojure.test :refer [is]]
            data))

;; call self

; https://practicalli.github.io/clojure/thinking-functionally/recursion.html

(defn length [coll]
  (if (empty? coll)
    0
    (+ 1 (length (rest coll)))))

(is (= 5 (length data/test-coll)))

(is (zero? (length [])))

(defn my-map [func a-list]
  (when a-list
    (cons (func (first a-list))
          (my-map func (next a-list))))) ; normally use rest, not next

(is (= (list 1 2 3 4 5) (my-map #(+ 1 %) data/test-coll)))

;; recur
(defn length-recur
  ([coll] (length-recur coll 0))
  ([coll cnt]
   (if (empty? coll)
     cnt
     (recur (rest coll) (+ 1 cnt)))))

(is (= 5 (length-recur data/test-coll)))

(is (zero? (length-recur [])))

; https://practicalli.github.io/clojure/thinking-functionally/tail-recursion.html

(defn sum
  ([coll] (sum coll 0))
  ([coll accum]
   (if (empty? coll)
     accum
     (recur (rest coll) (+ (first coll) accum)))))

(is (= 10 (sum data/test-coll)))

(is (zero? (sum [])))

;; recursion with nil return when empty instead of result
;; from Functional Programming for the Object-Oriented Programmer
(defn class-symbol-above
  "Lookup function that translates the lookup symbol into the next symbol or nil"
  [class-symbol]
  (case class-symbol
    RedPoint 'Point ;; test-constants are not evaluated, so the symbols should NOT be quoted
    Point 'Anything
    Anything 'Bicycle
    Bicycle 'Ride
    nil))

(def recursive-function
  (fn [class-symbol]
    (if (nil? class-symbol)
      nil
      (cons class-symbol (recursive-function (class-symbol-above class-symbol))))))

; same as:
(cons 'RedPoint (cons 'Point (cons 'Anything (cons 'Bicycle (cons 'Ride nil)))))

(recursive-function 'RedPoint)

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

;; various sum_upto examples
; +
(defn sum_upto_plus
  [nr]
  (apply + (range (inc nr))))

; recursion of function by making a range seq
(defn sum_upto_func
  ([nr] (sum_upto_func (range (inc nr)) 0)) ; or (take (inc nr) (range))
  ([nrcoll acc]
   (if (empty? nrcoll)
     acc
     (recur (rest nrcoll) (+ (first nrcoll) acc)))))

; recursion of function by dec nr using recur
(defn sum_upto_func_dec
  ([nr] (sum_upto_func_dec nr 0))
  ([nr acc]
   (if (= nr 0)
     acc
     (recur (dec nr) (+ nr acc)))))

; loop
(defn sum_upto_loop
  [nr]
  (loop [iter 1
         acc  0]
    (if (> iter nr)
      acc
      (recur (inc iter) (+ acc iter)))))

; reduce
(defn sum_upto_reduce
  [nr]
  (reduce + 0 (range (inc nr))))

; for, infinite
; If you are looking for how to write a loop in Clojure,
; I'm sorry, but this is not what you are looking for. 
; Clojure doesn't have an imperative loop because there 
; is no mutable local variable in Clojure..
; (defn sum_upto_for [])

; doing an infinite using a buildup of a vector
(def sum_upto_infinite
  (iterate
   (fn [[x acc]]
     [acc (+' x acc)])
   [0 1]))

; using coll input
;; (defn sum_upto_coll ; tried to do it Elixir style, doesn't work
;;   [head & rest]
;;   (if (empty? rest)
;;    rest
;;    (+ head (sum_upto_coll rest))))

;; (defn sum_upto_coll
;;   ([vals] (sum vals 0))
;;   ([vals acc]
;;    (if (empty? vals)
;;      acc
;;      (recur (rest vals) (+ (first vals) acc)))))

; destructure head and tail in func args
(defn sum_upto_coll
  ([vals] (sum vals 0))
  ([[head & tail] acc]
   (if (empty? vals)
     acc
     (recur tail (+ head acc)))))

(is (= 55
       (sum_upto_plus 10)
       (sum_upto_func 10)
       (sum_upto_func_dec 10)
       (sum_upto_loop 10)
       (sum_upto_reduce 10)
      ;  (sum_upto_for 10)
       (last (take 10 (map second sum_upto_infinite)))
       (second (last (take 10 sum_upto_infinite))) ; or take 2nd value of last
       ; sending a coll
       (sum_upto_coll (range (inc 10)))
       ))