(ns rich-hickey.reducers
  (:require [clojure.core.reducers :as r]))

(def v (into [] (range 10000000)))

; normal clojure fn's
; this loops over the coll 2x
(time (reduce + (map inc (filter even? v))))

; r/map & r/filter
; this loops over the coll 1x
(time (reduce + (r/map inc (r/filter even? v))))

; r/fold
; this parallelizes the loop over the coll 1x
(time (r/fold + (r/map inc (r/filter even? v))))

;; my own addition:

; transducers
(def even?-inc (comp (filter even?) (map inc)))

(time (transduce even?-inc + v))

; all-in-1 iterative
(count (filter even? v)) ; 5000000
(time (reduce (fn [acc nr]
                (if (even? nr)
                  (+ acc (inc nr))
                  ;;   (inc acc) ; do a count
                  acc))
              0 ; needed else number won't match above fn's
              v))

; all-in-1 parallel iterative - not faster than r/fold with r/map & r/filter
(time (r/fold + (fn [acc nr]
                  (if (even? nr)
                    (+ acc (inc nr))
                    acc))
              v))

; adjust n size (default 512) - not faster than normal r/fold group of 512
; needs the additional + as the combiner of the separate groups
(time (r/fold 10000 + + (r/map inc (r/filter even? v))))

; https://www.reddit.com/r/Clojure/comments/nxjeq3/can_transducers_use_forkjoin_under_the_hood_like/
; fold the transducer, but not so fast
; also eduction / transduce already actualize the data, unlike foldable solutions
(time (r/fold + (eduction even?-inc v)))

; now using r/folder and it's fast again..
(time (r/fold + (r/folder v even?-inc)))