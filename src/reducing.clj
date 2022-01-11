(ns reducing)

; from: https://dgr.github.io/clojurecrazy/2022/01/09/reduce-my-favorite-clojure-function.html

; without and with initial val:
(reduce + [1 2 3 4 5])
(reduce + 0 [1 2 3 4 5])

(reduce max [0 1 17 2 85 43 21 3 10])

(reduce min [0 1 17 2 85 43 21 3 10])

(reduce (fn [state input]
          (conj state ((fnil conj []) (last state) input)))
        []
        [1 2 3 4 5])

(reduce (fn [state input]
          (case state
            :start (case input
                     :a :saw-a
                     :b :saw-b)
            :saw-a (case input
                     :a :saw-a
                     :b :saw-b)
            :saw-b (case input
                     :a :saw-a
                     :b :saw-b-twice)
            :saw-b-twice :saw-b-twice))
        :start
        [:a :b :a :b :b :a :a :a :a])

; returns the last val of coll
(reduce (fn [_state input] input) [:a 1 "foo"])

; from: https://dgr.github.io/clojurecrazy/2022/01/10/using-reduce-to-implement-other-clojure-functions.html

(defn cc-into
  [to from]
  (reduce conj to from))

(cc-into {:a 1 :b 2} [[:c 3]])

(cc-into #{} [1 2 3 4 5 6])

(defn cc-filterv
  [pred coll]
  (reduce (fn [state input]
            (if (pred input)
              (conj state input)
              state))
          []
          coll))

(cc-filterv odd? (range 10))

(defn cc-mapv 
  [f coll]
  (reduce (fn [state input]
            (conj state (f input)))
          []
          coll))

(cc-mapv (partial * 5) (range 10))

(defn cc-keep
  [pred coll]
  (reduce (fn [acc x]
            (conj acc (pred x)))
          []
          coll))

(cc-keep even? (range 10))

(defn cc-remove
  [pred coll]
  (reduce (fn [acc x]
            (if-not (pred x)
              (conj acc x)
              acc))
          []
          coll))

(cc-remove pos? [1 -2 2 -1 3 7 0])
(cc-remove even? (range 10))

(defn cc-distinct
  [coll]
  (reduce (fn [acc x]
            ; lookup value in vector using Java .indexOf
            (if (neg? (.indexOf acc x)) ; .indexOf returns -1 if not found
              (conj acc x)
              acc))
          []
          coll))

(cc-distinct [1 1 2 3 4 4])

(defn cc-group-by
  [f coll]
  (reduce (fn [acc x]
            (let [k (f x)]
              ; val = conj to lookup key in acc, or [] if not found
              (assoc acc k (conj (get acc k []) x))))
          {}
          coll))

(cc-group-by count ["a" "as" "asd" "aa" "asdf" "qwer"])
