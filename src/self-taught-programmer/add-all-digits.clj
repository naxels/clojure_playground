(ns self-taught-programmer.add-all-digits
  (:require [clojure.test :refer [is]]
            [clojure.string]))

; puzzle from Self taught Programmer
; given a non-negative number, add up all the digits until there is only 1 digit left, return that digit

(defn int->seq
  [digits]
  (as-> (str digits) $ ; turn into string first
      (clojure.string/split $ #"") ; split on ""
      (map read-string $))) ; turn each char into int, map returns seq

(defn alt-int->seq
  [digits]
  (->> (str digits) ; turn into string
       (map (comp read-string str)))) ; run map
; concise version of (map str %) (map read-string %)
; comp does right to left

(defn sum
  "sum the sequence of ints"
  [int-seq]
  (apply + int-seq))

(defn add-digits
  [digits]
  (loop [x digits] ; recursively keep adding up digits
    (if (= 1 (count (str x))) ; until = 1
      x
      (recur (->> x
                  ; (#(do (println %) %)) ; print in between steps without interrupting, has side effects
                  int->seq ; turn into seq
                  sum))))) ; sum

(is (= 2 (add-digits 11))) ; 1+1=2

(is (= 9 (add-digits 99))) ; 9+9=18 1+8=9

(is (= 5 (add-digits 5)))

(is (= 9 (add-digits 999))) ; 9+9+9=27 2+7=9

(is (= 8 (add-digits 2345678765432234567)))

(comment
  (apply + (int->seq 99))
  (count (str 99))
  (class (first (seq (str 99))))
  (alt-int->seq 999))