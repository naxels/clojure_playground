(ns tracing
  (:require [clojure.tools.trace :as trace]))

(trace/trace (* 2 3))

(trace/trace-ns 'tracing) ; trace entire namespace

;; (trace/deftrace multisubst ; trace a specific function
(defn multisubst
  [new old lat]
  (cond
    (empty? lat) '()
    (= (first lat) old) (cons new (multisubst new old (rest lat)))
    :else (cons (first lat)
                (multisubst new old (rest lat)))))

(multisubst 'vegetables 'fruit '(the cats eat meat no fruit they do not like fruit apen))