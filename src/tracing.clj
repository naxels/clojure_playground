(ns tracing
  (:require [clojure.tools.trace :as trace]))

(trace/trace (* 2 3))

(trace/trace-ns 'tracing) ; trace entire namespace

; port from Racket/Scheme
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

(defn multisubstclj-two
  [new old lat]
  (if (empty? lat)
    '()
    (let [new-or-first (if (= (first lat) old)
                         new
                         (first lat))] 
         (cons new-or-first (multisubstclj-two new old (rest lat))))))

(multisubstclj-two 'vegetables 'fruit '(the cats eat meat no fruit they do not like fruit apen))

(defn length [collection]
  (if (empty? collection)
    0
    (+ 1 (length (rest collection)))))

(length [0 1 2 3])