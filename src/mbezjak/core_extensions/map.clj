(ns mbezjak.core-extensions.map
  "Missing functions in `clojure.core` that accept persistent map.

  Argument names and expected positions use the same convention as `clojure.core`.")

(defn map-key
  "Transform by calling `f` on each key of `m`.

  Always returns a new map of the same category as `m`."
  [m f]
  (->> m
       (map (fn [[k v]] [(f k) v]))
       (into (empty m))))

(defn map-val
  "Transform by calling `f` on each value of `m`.

  Always returns a new map of the same category as `m`."
  [m f]
  (->> m
       (map (fn [[k v]] [k (f v)]))
       (into (empty m))))

(defn filter-key
  "Filter entries of `m` satisfying `(pred k)`.

  Always returns a new map of the same category as `m`."
  [m pred]
  (->> m
       (filter (comp pred key))
       (into (empty m))))

(defn filter-val
  "Filter entries of `m` satisfying `(pred v)`.

  Always returns a new map of the same category as `m`."
  [m pred]
  (->> m
       (filter (comp pred val))
       (into (empty m))))

(defn remove-key
  "Removes entries of `m` satisfying `(pred k)`.

  Always returns a new map of the same category as `m`."
  [m pred]
  (filter-key m (comp not pred)))

(defn remove-val
  "Removes entries of `m` satisfying `(pred v)`.

  Always returns a new map of the same category as `m`."
  [m pred]
  (filter-val m (comp not pred)))

(defn remove-keys
  "Opposite of `clojure.core/select-keys`."
  [m ks]
  (apply dissoc m ks))

(defn dissoc-in
  "Dissociate a key with path `ks` in a nested associated structure `m`."
  [m ks]
  (cond
    (zero? (count ks)) m
    (= 1 (count ks)) (dissoc m (first ks))
    :else
    (let [[k & rks] ks]
      (if (contains? m k)
        (update m k #(dissoc-in % rks))
        m))))

(defn update-when
  "Update the value of `k` in `m` with `f` only when `(pred v)`."
  [m k pred f]
  (if (pred (get m k))
    (update m k f)
    m))

(defn update-if
  "Update the value of `k` in `m` with `f` only if `k` exists."
  [m k f]
  (if (contains? m k)
    (update m k f)
    m))

(defn assoc-if
  "Override the value of `k` in `m` with `v` only if `k` exists."
  [m k v]
  (if (contains? m k)
    (assoc m k v)
    m))

(defn submap?
  "Does `m` contain all keys and their vals of `n`?"
  [m n]
  (= n (select-keys m (keys n))))

(defn unqualified
  "Convert map keys to unqualified keywords.

  Assumes map keys are keywords."
  [m]
  (map-key m (comp keyword name)))