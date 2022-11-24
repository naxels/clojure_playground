(ns mbezjak.core-extensions.number
  "Missing functions in `clojure.core` that accept a number."
  (:import
   (java.math RoundingMode)))

(defn constrain
  "Make `x` fit the bounds `minimum` and `maximum`."
  [minimum maximum x]
  (max minimum (min maximum x)))

(defn set-scale
  "Sets the scale of `x` to `n`.

  `n` must be positive integer. Always returns a `double`.

  See also `BigDecimal/setScale`."
  ^double [^Integer n x]
  (-> x
      (double)
      (bigdec)
      (.setScale n RoundingMode/HALF_UP)
      (.doubleValue)))

;; https://en.wikipedia.org/wiki/Foot_(unit)
(def ^:private conversion-factor-ft->cm 30.48)

;; https://en.wikipedia.org/wiki/Pound_(mass)
(def ^:private conversion-factor-lb->kg 0.45359237)

(defn cm->ft [x]
  (/ x conversion-factor-ft->cm))

(defn ft->cm [x]
  (* x conversion-factor-ft->cm))

(defn kg->lb [x]
  (/ x conversion-factor-lb->kg))

(defn lb->kg [x]
  (* x conversion-factor-lb->kg))