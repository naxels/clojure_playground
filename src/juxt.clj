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

(let [[count min max sum avg] (summarize [1 20 42 108])] ; with destructuring
  (is (= 4 count))
  (is (= 1 min))
  (is (= 108 max))
  (is (= 171 sum))
  (is (= 171/4 avg)))


;; As keywords are functions we can use them to get values
;; for keys in a map in the order we want with juxt:
(is (= ["Hubert" "mrhaki"]
       ((juxt :name :alias) {:alias "mrhaki" :name "Hubert" :location "Tilburg"})))

; from: https://lambdaisland.com/blog/2019-12-04-advent-of-parens-4-a-useful-idiom
; turn keys into keywords
(defn keywordize-keys
  [m]
  (into {} (map (juxt (comp keyword key) val)) m))

(keywordize-keys {"x" 1 "y" 2})

(defn map-vals
  "Maps a function over the values of an associative collection."
  [f m]
  (into {} (map (juxt key (comp f val))) m))

(map-vals inc {:x 1 :y 2})

; group-by alternative without putting each map into it's own vector
(let [coll [{:id 456 :x "hello"}
            {:id 641 :x "world"}
            {:id 941 :x "wide"}]]
  (into {} (map (juxt :id identity)) coll))