(ns tracing
  (:require [clojure.tools.trace :as trace]))

(trace/trace (* 2 3))

(trace/trace-ns 'tracing) ; trace entire namespace

; port from Racket/Scheme
; recursion without accumulation

;; (trace/deftrace multisubst ; trace a specific function
(defn multisubst
  [new old lat]
  (cond
    (empty? lat) '()
    (= (first lat) old) (cons new (multisubst new old (rest lat)))
    :else (cons (first lat)
                (multisubst new old (rest lat)))))

(multisubst 'vegetables 'fruit '(the cats eat meat no fruit they do not like fruit apen))

(defn multisubstclj
  [new old lat]
  (if (empty? lat)
    '()
    (if (= (first lat) old)
      (cons new (multisubstclj new old (rest lat)))
      (cons (first lat)
            (multisubstclj new old (rest lat))))))

(multisubstclj 'vegetables 'fruit '(the cats eat meat no fruit they do not like fruit apen))

; in a more clojure style:
(defn multisubstclj-two
  [new old lat]
  (if (empty? lat)
    '()
    (let [f-lat (first lat)
          new-or-first (or (when (= f-lat old) new)
                           f-lat)]
         (conj (multisubstclj-two new old (rest lat)) new-or-first))))

(multisubstclj-two 'vegetables 'fruit '(the cats eat meat no fruit they do not like fruit apen))

(defn multisubstclj-loop
  [new old lat]
  (loop [my-result '()
         my-lat (reverse lat)] ; needed because we process by adding on the front
    (if (empty? my-lat)
      my-result
      (recur (let [f-lat (first my-lat)
                   new-or-first (or (when (= f-lat old) new)
                                    f-lat)] (conj my-result new-or-first))
             (rest my-lat)))))

(multisubstclj-loop 'vegetables 'fruit '(the cats eat meat no fruit they do not like fruit apen))

; using a map to replace a value with another
(replace {'vegetables 'fruit} '(the cats eat meat no fruit they do not like fruit apen))

; borrowing from the replace source code, doing a map makes a lot of sense since go over each value
(map #(if-let [e (find {'vegetables 'fruit} %)] (val e) %) '(the cats eat meat no fruit they do not like fruit apen)) ; returns the val of map 'fruit

; need to create a closure that returns the reduce function with old and new applied
; tracing doesn't work on reduce, use reductions:
(reductions (fn [so-far val]
          (let [new-or-val (or (when (= val 'fruit) 'vegetables)
                               val)]
            (conj so-far new-or-val)))
        '()
        (reverse '(the cats eat meat no fruit they do not like fruit apen))) ; need to again use reverse

(defn length [collection]
  (if (empty? collection)
    0
    (+ 1 (length (rest collection)))))

(length [0 1 2 3])