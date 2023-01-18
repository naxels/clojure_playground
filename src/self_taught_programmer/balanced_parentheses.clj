(ns self-taught-programmer.balanced-parentheses
  (:require [clojure.test :refer [is]]))

; puzzle from Self taught Programmer
; make sure the opening and closing parentheses equal out.
; return true when yes, false when no

; idea: turn into frequences, check if both present, then check if both match

; (defn is-equal
;   [result-seq]
;   (let [first-val (val (first result-seq)) ; not very efficient
;         second-val (val (second result-seq))]
;     (if (= first-val second-val)
;       true
;       false)))

; better way :)
(defn is-equal
  [result-seq]
  (if (apply = (vals result-seq)) ; apply = to each value from map
    true
    false))

(defn is-balanced
  [result-seq]; LazySeq containing map
;   (println (class (first result-seq)))
  (let [c (count result-seq)]
    (if (not (= c 2)) ; anything other than 2 for paren count is wrong
      false
      (is-equal result-seq))))

(defn parentheses?
  [[k _v]] ; k = character
  (or (= (str k) "(")
      (= (str k) ")")))

(defn parens-balanced
  [str-with-parens]
  (->> str-with-parens
       (frequencies)
    ;    (#(do (println %) %))
       (filter parentheses?) ; throw out anything but parens
       ; (#(do (println %) %))
       (is-balanced)))

(is (= true (parens-balanced "()()()")))

(is (= false (parens-balanced "()()(")))

(is (= true (parens-balanced "(())")))

(is (= true (parens-balanced "(bl)(a)(a)")))

(is (= true (parens-balanced "((king))")))

(is (= false (parens-balanced "(n)(o)(")))

(is (= false (parens-balanced ")))")))

(is (= false (parens-balanced "okiedokie")))

(comment
  (let [a (frequencies "()()()")]
    (map (fn [[k _v]] (class k)) a))
  
  ([\( 2] [\) 2]))