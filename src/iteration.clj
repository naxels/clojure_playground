(ns iteration
  (:require [data]))

; Clojure core iteration is used for parsing paginated API's, but also works great for local datasets

(def example-data {:a {:next-token :b
                       :val  "A"}
                   :b {:next-token :c
                       :val  "B"}
                   :c {:next-token :d
                       :val "C"}
                   :d {:val "D"}}) ; No :next-token, so iteration will end.

; iteration returns a lazy seq, vec realizes it
(-> (iteration
     (fn [token]
       (println token)
       (get example-data token)) ; step fn
     :vf    :val        ; value fn
     :kf    :next-token ; token key fn
     :initk :a)         ; first token
    vec)

; example traversing a hierarchy
(defn ancestors-of
  [people-db parent-kw person-name]
  (iteration
   (fn [name] (find people-db name)) ; step: lookup name in coll
   :vf    key ; value = key of find result
   :kf    (fn [[_name parents]] (parent-kw parents))
   :initk person-name))

; seq realizes the lazy seq
(seq (ancestors-of data/people :dad "Carl"))

(seq (ancestors-of data/people :mom "Carl"))