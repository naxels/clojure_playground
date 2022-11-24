(ns mbezjak.core-extensions.pair
  "A pair is a vector of size 2.

  Functions here preserve the vector form.")

(defn map-1
  "Transform first element of `p` using `f`."
  [[a b] f]
  [(f a) b])

(defn map-2
  "Transform second element of `p` using `f`."
  [[a b] f]
  [a (f b)])

(defn map-3
  "Transform first element of `p` using `f1` and second element of `p` using `f2`."
  [[a b] f1 f2]
  [(f1 a) (f2 b)])