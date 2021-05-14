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

; Udemy Clojure from beginner to advanced
(defn make-inc
  []
  (fn [x] (+ 1 x)))

;; (make-inc 2) ; doesn't work since make-inc doesn't take args
(def my-inc (make-inc)) ; gets the func from make-inc and assigns it to my-inc
(my-inc 2) ; 3

(defn make-inc-with-arg
  [x]
  (fn [y] (+ y x)))

(def inc-5 (make-inc-with-arg 5)) ; like partial, returns a func with 1 arg filled
(def inc-8 (make-inc-with-arg 8))
(inc-5 5) ; 10
(inc-8 5) ; 13

; function as argument
(defn test-and-inc
  [tst-fn x]
  (if (tst-fn x)
    (inc x)
    x))

(test-and-inc odd? 3) ; 4
(test-and-inc odd? 4) ; 4

(defn my-apply-two
  [f1 f2 arg]
  (f1 (f2 arg)))

(my-apply-two inc inc 5) ; 7
(my-apply-two inc #(* 5 %) 5) ; 26

(defn my-comp [f1 f2]
  (fn [arg] (f1 (f2 arg))))

(def times-five-inc (my-comp inc #(* 5 %))) ; returns func
(times-five-inc 5) ; 26

(let [a 5]
  (defn foo [b]
    (* b a)))

(foo 5) ; 25

;; my own trials
(defn hospital-staff-for-position
  [position]
  (filter #(= (:position %) position) (vals (:staff data/hospital))))

(def hospital-residents (hospital-staff-for-position :resident))

(defn combine-two-datapoints
  [f]
  (if (= print f) ; so easy to compare incoming function
    (fn [d1 d2] (f d1 d2))
    (fn [d1 d2] (f d1 " " d2))))

(def combine-names-by-str (combine-two-datapoints str))

(def combine-names-by-print (combine-two-datapoints print))

(defn get-names
  [coll]
  (combine-names-by-str (:first-name coll) (:last-name coll)))

(defn print-names
  [coll]
  (combine-names-by-print (:first-name coll) (:last-name coll)))

(map get-names hospital-residents)
(map print-names hospital-residents)

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