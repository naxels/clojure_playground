(ns reducing
  (:require [clojure.core.reducers :as r]
            [clojure.core.protocols :as p]))

; from: https://dgr.github.io/clojurecrazy/2022/01/09/reduce-my-favorite-clojure-function.html

; without and with initial val:
(reduce + [1 2 3 4 5])
(reduce + 0 [1 2 3 4 5])

(reduce max [0 1 17 2 85 43 21 3 10])

(reduce min [0 1 17 2 85 43 21 3 10])

(reduce (fn [state input]
          (conj state ((fnil conj []) (last state) input)))
        []
        [1 2 3 4 5])

(reduce (fn [state input]
          (case state
            :start (case input
                     :a :saw-a
                     :b :saw-b)
            :saw-a (case input
                     :a :saw-a
                     :b :saw-b)
            :saw-b (case input
                     :a :saw-a
                     :b :saw-b-twice)
            :saw-b-twice :saw-b-twice))
        :start
        [:a :b :a :b :b :a :a :a :a])

; returns the last val of coll
(reduce (fn [_state input] input) [:a 1 "foo"])

; from: https://dgr.github.io/clojurecrazy/2022/01/10/using-reduce-to-implement-other-clojure-functions.html

(defn cc-into
  [to from]
  (reduce conj to from))

(cc-into {:a 1 :b 2} [[:c 3]])

(cc-into #{} [1 2 3 4 5 6])

(defn cc-filterv
  [pred coll]
  (reduce (fn [state input]
            (if (pred input)
              (conj state input)
              state))
          []
          coll))

(cc-filterv odd? (range 10))

(defn cc-mapv
  [f coll]
  (reduce (fn [state input]
            (conj state (f input)))
          []
          coll))

(cc-mapv (partial * 5) (range 10))

(defn cc-keep
  [pred coll]
  (reduce (fn [acc x]
            (conj acc (pred x)))
          []
          coll))

(cc-keep even? (range 10))

(defn cc-remove
  [pred coll]
  (reduce (fn [acc x]
            (if-not (pred x)
              (conj acc x)
              acc))
          []
          coll))

(cc-remove pos? [1 -2 2 -1 3 7 0])
(cc-remove even? (range 10))

(defn cc-distinct
  [coll]
  (reduce (fn [acc x]
            ; lookup value in vector using Java .indexOf
            (if (neg? (.indexOf acc x)) ; .indexOf returns -1 if not found
              (conj acc x)
              acc))
          []
          coll))

(cc-distinct [1 1 2 3 4 4])

(defn cc-group-by
  [f coll]
  (reduce (fn [acc x]
            (let [k (f x)]
              ; val = conj to lookup key in acc, or [] if not found
              (assoc acc k (conj (get acc k []) x))))
          {}
          coll))

(cc-group-by count ["a" "as" "asd" "aa" "asdf" "qwer"])

; https://levelup.gitconnected.com/reducers-in-clojure-c088a5627412?gi=3e4cb8cc8d70

(reduce str [1 2 3 4 5 6])
(reductions str [1 2 3 4 5 6])

(reduce (fn [_result input]
          (if (< input 5)
            (println input) ; side effects
            (reduced "finished")))
        (range)) ; infinite range

(defn mapping [f]
  (fn [f1]
    (fn [result input]
      (f1 result (f input)))))

; apply inc to mapping, + to the resulting function and returns the newly fn
;; (defn mapping [inc]
;;   (fn [f1]
;;     (fn [result input]
;;       (f1 result (inc input)))))

;; (fn [+]
;;   (fn [result input]
;;     (+ result (inc input))))

;; return is a reducing fn of 2 args
;; (fn [result input]
  ;; (+ result (inc input)))

(def mapping-inc-+ ((mapping inc) +))

(reduce mapping-inc-+ 0 [1 2 3 4])
; Benefit = decoupling transformation from collection

(defn my-reducer
  "Given a reducible collection, and a transformation function xf,
  returns a reducible collection, where any supplied reducing
  fn will be transformed by xf. xf is a function of reducing fn to
  reducing fn."
  ([coll xf]
   (println "my-reducer: called with coll " (str coll))
   (reify
     p/CollReduce
     (p/coll-reduce [this f1]
       (p/coll-reduce this f1 (f1)))
     (p/coll-reduce [_ f1 init]
       (p/coll-reduce coll (xf f1) init)))))

(defn my-mapping [f]
  (fn [f1]
    (fn [result input]
      (println "my-mapping: calling f1 on the result and f on our input for [" (str result) "," (str input) "]")
      (f1 result (f input)))))

(reduce + 0 (my-reducer [1 2 3 4] (my-mapping inc)))
;; [0, 1] => (+ 0 (inc 1)) == 2
;; [2, 2] => (+ 2 (inc 2)) == 5
;; [5, 3] => (+ 5 (inc 3)) == 9
;; [9, 4] => (+ 9 (inc 4)) == 14

;; (reduce f init coll)
;; f - The function that takes two inputs [result input] and produces the result for each step in the result
;; init - The initial result value
;; coll - The collection to reduce

;; With our reducer function, we are calling:
;; (reducer coll f)
;; f - The function which will reduce over the collection coll
;; coll - The collection to operate on

(defn my-filtering [pred]
  (println "my-filtering called with predicate " (str pred))
  (fn [f1]
    (fn [result input]
      (println "my-filtering now operating on [result, input] [" (str result) "," (str input) "]")
      (if (pred input)
        (f1 result input)
        result))))

(reduce + 0 (my-reducer [1 2 3 4 5 6] (my-filtering even?)))
;; [0, 1] => (even? 1) == false, so return result 0
;; [0, 2] => (even? 2) == true, so return (+ result 2) = 2
;; [2, 3] => (even? 3) == false, so return result 2
;; [2, 4] => (even? 4) == true, so return (+ result 4) = 6
;; [6, 5] => (even? 5) == false, so return result
;; [6, 6] => (even? 6) == true, so return (+ result 6) = 12

(reduce + 0 (-> [1 2 3 4 5 6]
                (my-reducer (my-filtering even?))
                (my-reducer (my-mapping inc))))

; Let’s define some helper functions:
(defn rmap [f coll]
  (my-reducer coll (my-mapping f)))

(defn rfilter [pred coll]
  (my-reducer coll (my-filtering pred)))

(reduce + 0 (->> [1 2 3 4 5 6]
                 (rfilter even?)
                 (rmap inc)))

; trying myself with comp/transduce

; very strange: comp should work from right to left, but in this case, it presents wrong answer
; so have to put inc first, then even?..
(def transduce-trial (comp
                      (filter even?)
                      (map inc)))

(transduce transduce-trial + 0 [1 2 3 4 5 6])

(transduce transduce-trial conj [1 2 3 4 5 6])
