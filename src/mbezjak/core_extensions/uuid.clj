(ns mbezjak.core-extensions.uuid
  "Missing functions for java.util.UUID."
  (:import java.util.UUID))

(defn from-string [s]
  (UUID/fromString s))

(defn coerce
  "Coerce `x` into java.util.UUID."
  [x]
  (cond
    (string? x) (from-string x)
    (uuid? x) x
    :else (throw (ex-info "Don't know how to coerce a value into java.util.UUID"
                          {:value x
                           :type (type x)}))))