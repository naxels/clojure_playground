(ns clojure-workshop.obfuscation-machine
  (:require [clojure.string]))

(defn encode-letter
  [s x]
  #_(println s)
  (let [code (Math/pow (+ x (int (first (char-array s)))) 2)]
    (str "#" (int code))))

(defn encode
  [s]
  (let [word-count (count (clojure.string/split s #" "))]
  (clojure.string/replace s #"\w" (fn [s] (encode-letter s word-count)))))

(defn decode-letter
  [x y]
  (let [number (Integer/parseInt (subs x 1))
        letter (char (- (Math/sqrt number) y))]
    (str letter)
  ))

(defn decode 
  [s]
  (let [word-count (count (clojure.string/split s #" "))]
    (clojure.string/replace s #"\#\d+" (fn [s] (decode-letter s word-count)))))