(ns mbezjak.core-extensions.coll
  "Missing functions in `clojure.core` that accept a collection.

  Argument names and expected positions use the same convention as `clojure.core.`"
  (:refer-clojure :exclude [any?]))

;; https://clojuredocs.org/clojure.core/any_q - comment
(def
  ^{:tag Boolean
    :doc "Returns true if (pred x) is logical true for any x in coll,
  else false."
    :arglists '([pred coll])
    :added "1.7"}
  any? (comp boolean some))

;; (defn any?
;;   "Opposite of `clojure.core/not-any?`.

;;   Returns `false` for empty `coll`."
;;   [pred coll]
;;   (boolean (some pred coll)))

(defn includes?
  "Does `coll` include `x`?

  Guaranteed to return a boolean. Time complexity linear, so don't use for large
  collections."
  [x coll]
  (any? #(= x %) coll))

(defn elem
  "Find the first element matching `pred` in `coll`.

  Returns `nil` on no match. Time complexity linear, so don't use for large
  collections."
  [pred coll]
  (some #(when (pred %) %) coll))

(defn groups
  "Group elements in `coll` together based on the value returned by `f`.

  Group order is not guaranteed. Element order inside each group is guaranteed
  to be stable. Useful when the value given by `f` is not needed. Returns `nil`
  for empty collections.

  Example:
  (= '([{:id 1 :name :a} {:id 1 :name :c}]
       [{:id 2 :name :b}])
     (groups :id [{:id 1 :name :a}
                  {:id 2 :name :b}
                  {:id 1 :name :c}]))"
  [f coll]
  (vals (group-by f coll)))

(defn separate-2
  "Separate `coll` into two collections based on boolean value of `f`.

  Returns a pair [those-that-satisfy-f those-that-do-not-satisfy-f].
  Will always return a pair, even if one of the collections are `nil`.
  Guarantees stable order of each collection."
  [pred coll]
  (let [g (group-by (comp boolean pred) coll)]
    [(get g true) (get g false)]))

(defn adjacent-pairs
  "Returns pairs of adjacent (to the right) elements in `coll`.

  Example:
  (= [[1 2] [2 3]]
     (adjacent-pairs [1 2 3]))"
  [coll]
  (mapv vector coll (rest coll)))

(defn combinations-2
  "Returns a lazy sequence of all possible pairs in `coll`.

  Returns `nil` if no pairs exist."
  [coll]
  (when (seq coll)
    (let [[x & rst] coll
          other (lazy-seq (combinations-2 rst))]
      (seq
       (lazy-cat (map vector (repeat x) rst)
                 other)))))

(defn splits-by
  "Splits `coll` into multiple groups each time `split-fn` returns `true`."
  [split-fn coll]
  (loop [coll coll
         splits []]
    (if-not (seq coll)
      splits
      (let [[f l] (split-with (complement split-fn) coll)]
        (recur (rest l) (conj splits f))))))