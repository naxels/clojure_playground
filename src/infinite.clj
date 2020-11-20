(ns infinite
  (:require [clojure.test :refer [is]]
            data))

;; cycle
; will cycle through the collection infinitely
(defn give_candy
  [candy-coll to-coll]
  (zipmap to-coll (cycle candy-coll))) ; will stop when any of the colls finishes

(is (= {"Mike" "chocolate"
        "Anna" "jelly"
        "Ted" "mint"
        "Mary" "chocolate"
        "Alex" "jelly"
        "Emma" "mint"
        "Piet" "chocolate"}
       (give_candy data/candy data/kids)))