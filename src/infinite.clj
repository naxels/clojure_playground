(ns infinite
  (:require [clojure.test :refer [is]]))

;; cycle
; will cycle through the collection infinitely
(def candy '("chocolate" "jelly" "mint"))
(def kids '("Mike" "Anna" "Ted" "Mary" "Alex" "Emma" "Piet"))

(defn give_candy
  [candy-coll to-coll]
  (zipmap kids (cycle candy-coll))) ; will stop when any of the colls finishes

(is (= {"Mike" "chocolate"
        "Anna" "jelly"
        "Ted" "mint"
        "Mary" "chocolate"
        "Alex" "jelly"
        "Emma" "mint"
        "Piet" "chocolate"}
       (give_candy candy kids)))