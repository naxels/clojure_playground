(ns juxt
  (:require [clojure.test :refer [is]]))

;; The functions first, last and count are applied
;; to the string value and we get a vector
;; with the result of each function.
(is (= [\C \e 18]
       ((juxt first last count) "Clojure is awesome")))


(defn sum
  "Calculate sum of values in collection"
  [coll]
  (apply + coll))

(defn avg
  "Calculate average of values in collection"
  [coll]
  (/ (sum coll) (count coll)))

;; Create new function summarize to give back statistics
;; on a collection based on count, min, max, sum and average.
(def summarize (juxt count #(apply min %) #(apply max %) sum avg))

(is (= [4 1 108 171 171/4]
       (summarize [1 20 42 108])))

(let [[count min max sum avg] (summarize [1 20 42 108])]
  (is (= 4 count))
  (is (= 1 min))
  (is (= 108 max))
  (is (= 171 sum))
  (is (= 171/4 avg)))


;; As keywords are functions we can use them to get values
;; for keys in a map.
(is (= ["Hubert" "mrhaki"]
       ((juxt :name :alias) {:alias "mrhaki" :name "Hubert" :location "Tilburg"})))