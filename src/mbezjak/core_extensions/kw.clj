(ns mbezjak.core-extensions.kw
  "Missing functions in `clojure.core` that accept a keyword."
  (:refer-clojure :exclude [str]))

(defn str
  "Makes a string out of `k` so that it can be shared to non-clojure world.

  Preserves namespace info if present."
  [k]
  (if (qualified-keyword? k)
    (format "%s/%s" (namespace k) (name k))
    (name k)))