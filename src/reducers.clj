(ns reducers
  (:require [clojure.core.reducers :as r]))

; Reducers like Transducers try to separate operation from context

;; Lists aren’t foldable

; this won't work as it takes all operations together before finally executing
; (r/map inc [1 2])
; only after realizing the result, an fn like (first) will work

; reducer is going to accept a collection coll and a transformation function xf,
; and return a reducer that provides data to the reduce function.
(r/reduce + 0 (r/reducer [1 2 3 4] (map inc)))

(into [] (r/map inc [1 2]))

;; https://www.braveclojure.com/quests/reducers/know-your-reducers/

; this will combine r/filter and r/map and try to do things in parallel,
; making it faster than the separate operations. (only on large non-lazy transformations)
(time (->> (range 10000000)
           (r/filter even?)
           (r/map inc)
           (into [])))

; Clojure will divide your vector of ten million numbers into sub vectors of 512 elements,
; run (fold + subvector) on those subvectors in parallel, and combine the results.
(time (r/fold + (vec (range 10000000))))
