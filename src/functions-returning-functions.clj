(ns functions-returning-functions
  (:require [clojure.test :refer [is]]))

;; partial
(defn my-square [arg1 arg2]
  (* arg1 arg2))

(def always-five
  (partial my-square 5)) ; partial can provide the function and x args

(is (= 50 (always-five 10)))

(def add1 (partial + 1)) ; this function works by itself because + can take 1 arg

(is (= 3 (add1 2)))

;; comp ; from right to left apply f
(def my-square-and-add1 (comp always-five add1))
; (def ccat-and-reverse (comp (partial apply str) reverse str))

(is (= 10 (my-square-and-add1 1))) ; (+ 1 'arg') -> (my-square 5 'result')

;; juxt
; Takes a set of functions and returns a fn that is the juxtaposition
; of those fns.  The returned fn takes a variable number of args, and
; returns a Vector containing the result of applying each fn to the
; args (left-to-right) .
(is (= [1 2] ((juxt :a :b) {:a 1 :b 2 :c 3 :d 4})))
; (:a {data}) , (:b {data}) , then combine

(apply (juxt + *) [1 2]) ; apply first + then * to data set
;same as:
[(apply + [1 2]) (apply * [1 2])]

; Get the first, last character and length of string
(is (= [\C \s 13] ((juxt first last count) "Clojure Rocks")))

; simple design salary transformer to demonstrate juxt
(defn salary-monthly->yearly [salary]
  (* salary 12))

(defn salary-monthly->weekly [salary]
  (-> salary ; -> is do as first argument, ->> as last
      (salary-monthly->yearly)
      (/ 52)))

(defn salary-monthly->daily [salary]
  (-> salary
      (salary-monthly->weekly)
      (/ 5)))

(defn salary-monthly->hourly [salary]
  (-> salary
      (salary-monthly->daily)
      (/ 8)))

(is (= [12000 3000/13 600/13 75/13] ((juxt salary-monthly->yearly
                salary-monthly->weekly
                salary-monthly->daily
                salary-monthly->hourly) 1000)))

; (float $)
; (format "%.2f" $)

; partial application is also visible in chaining:
; (-> a
    ; b
    ; c)