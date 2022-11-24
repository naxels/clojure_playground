(ns mbezjak.core-extensions.pairs
  "Pairs is a collection of `pair`."
  (:require
   [mbezjak.core-extensions.pair :as pair]))

(defn map-1
  "Transform first element of `ps` using `f`."
  [f ps]
  (map #(pair/map-1 % f) ps))

(defn map-2
  "Transform second element of `ps` using `f`."
  [f ps]
  (map #(pair/map-2 % f) ps))

(defn filter-1
  "Filter `ps` where first element satisfies `pred`."
  [pred ps]
  (filter (fn [[a _]] (pred a)) ps))

(defn filter-2
  "Filter `ps` where second element satisfies `pred`."
  [pred ps]
  (filter (fn [[_ b]] (pred b)) ps))

(defn firsts
  "Take the first elements of `ps`."
  [ps]
  (map first ps))

(defn seconds
  "Take the second elements of `ps`."
  [ps]
  (map second ps))