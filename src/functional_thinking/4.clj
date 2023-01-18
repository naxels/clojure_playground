(ns functional-thinking.4
  (:require [clojure.string :refer [split]]))


; rot13 with memoize
(let [alpha (into #{} (concat (map char (range (int \a) (inc (int \z))))
                              (map char (range (int \A) (inc (int \Z)))))) ; #{\A \a \B \b \C \c \D \d \E \e \F \f ...}
      rot13-map (zipmap alpha ; zipmap outputs map with key value
                        (take 52 ; take 52 from the point of dropping 26 items
                              (drop 26 ; drop 26 from the list of infinite repetition
                                    (cycle alpha))))] ; cycle just keeps repeating the coll infinitly
;; (def a rot13-map)
  (defn rot13
    "Given an input string, produce the rot 13 version of the string. \"hello\" -> \"uryyb\""
    [s]
    (apply str (map #(get rot13-map % %) s))))

(defn name-hash
  [name]
  (apply str (map #(rot13 %) (split name #"\d"))))

(def name-hash-m (memoize name-hash)) ; memoizes the calculated values, so lookups are speed up

(name-hash "hello")
(name-hash-m "hello")
(name-hash-m "heeeeeeelloooooooo")
(name-hash-m "HEEEEEEELLOOOOOOOO")

; palindrome
(defn palindrome?
  [s]
  (let [sl (.toLowerCase s)]
    (= sl (apply str (reverse sl)))))

(defn find-palindromes
  [s]
  (filter palindrome? (split s #" ")))

(find-palindromes "The quick brown fox jumped over anna the dog")
(find-palindromes "Bob went to Harrah and gambled with Otto and Steve")
(take 1 (find-palindromes "Bob went to Harrah with Otto and Steve"))