(ns custom-handy-fns
  (:require [data]))

(defn associate-by
  "Adapted from group-by:
   Returns a map of the elements of coll keyed by the result of
   f on each element. The value at each key is the corresponding
   element"
  [f coll]
  (persistent!
   (reduce
    (fn [acc x]
      (assoc! acc (f x) x))
    (transient {})
    coll)))

(= (associate-by even? [1 2 3 4 5])
   {false 5, true 4})

(= (keys (associate-by :name data/customers))
   '("henk" "piet" "jaap"))