(ns partial-apply
  (:require [data]))

;; example from Functional Design in Clojure epi 078

;; given 2 datasets (maps) find out if they are the same
;; map with juxt, key for first field, key for 2nd field, returns list of 2 vals per vec
;; ^ created my own 2 data sets

;; run with partial apply = , results in a list of true/false statements
;; then you can see the results
;; with frequencies you see how many true/false

(def combined (map vector data/list-one data/list-two))
(def truefalse-int (map (partial apply =) combined))
(frequencies truefalse-int)

(def truefalse-keys (map (partial apply =) data/list-vec-combined))
(frequencies truefalse-keys)