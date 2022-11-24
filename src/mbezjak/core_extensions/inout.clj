(ns mbezjak.core-extensions.inout
  "Missing functions in `clojure.java.io`."
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io]))

(defn read-bytes [streamable]
  (with-open [is (io/input-stream streamable)]
    (.readAllBytes is)))

(defn read-edn [readable]
  (with-open [r (io/reader readable)]
    (edn/read-string (slurp r))))