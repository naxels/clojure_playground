(ns functions-returning-functions
  (:require [clojure.test :refer [is]]
            [clojure.string :as str]
            data))

; you really have to see functions as values..

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

(def camel->keyword-comp (comp keyword
                               str/join
                               (partial interpose \-)
                               (partial map str/lower-case)
                               #(str/split % #"(?<=[a-z])(?=[A-Z])")))

(is (= :camel-case (camel->keyword-comp "CamelCase")))

; same example with ->>
(defn camel->keyword
  [s]
  (->>
   (str/split s #"(?<=[a-z])(?=[A-Z])")
   (map str/lower-case)
   (interpose \-)
   str/join
   keyword))

(is (= :camel-case (camel->keyword "CamelCase")))

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

;; functions that returns a function
(defn adder
  [n]
  (fn [x] (+ n x)))

; (adder 5) ; result is function
((adder 5) 18) ; now gives output

(defn doubler
  [f]
  (fn [& args]
    (* 2 (apply f args))))

(def double-+ (doubler +)) ; again returns a function, now assigned to variable

(double-+ 1 2 3) ; and now the result

;; Closures
; When a function (let's call this inner function) 
; is returned from another function (let's call this outer function), 
; and the inner function does somethings with the arguments given 
; from outer function, then the inner function is called a closure.
(defn inner-function
  [from-outer]
  (fn [] (+ 1 from-outer)))

; returns a function that going forward takes 0 arguments
(def outer-function1 (inner-function 10))

(def outer-function2 (inner-function 200))

(outer-function1)
(outer-function2)

; another
(defn range-to
  [until]
  (fn [] (range 1 (+ 1 until))))

(def range-to-ten (range-to 10))

(range-to-ten)

; returning a map field
(defn keyword-mapper
  [keywrd]
  (fn [coll] (map keywrd coll)))

(def age-mapper (keyword-mapper :age))
(def name-mapper (keyword-mapper :name))

(age-mapper data/persons)
(name-mapper data/persons)

;; constantly
(def boring (constantly 10))

(boring 1 2 3)

(reduce + (map (constantly 1) [:a :b :c]))

(map (constantly 9) [1 2 3])