(ns conditionals
  (:require [data]))

;; Very interesting because you can 'stack' the function calls like with + / - * etc

;; and
(let [a true
      b false]
  (and a b)
  
  ; equivalent of
  (if (= a true)
    (if (= b true)
      true
      false)
    false))

;; or
(let [target data/enemy]
  (or (:armor target) 0)

; equivalent of
  (if (:armor target) ; testing if :armor exists
    (:armor target) ; if true, return that value
    0) ; else return 0
  )

; great check for nil value then take other value
(let [n nil]
  (inc (or n 0)))

; equivalent of
(let [n nil]
  (inc (if n
         n
         0)))

; another way that duplicates inc
;; (let [n nil]
;;   (if n
;;     (inc n) ; clj-kondo: type error
;;     (inc 0)))

;; =
(let [a 0
      b 0]
  (if (= a b 1) 0 1) ; return 0 when any of a or b is 1

  ; equivalent of
  (if (or (= a 1)
          (= b 1))
    0
    1))

(defn sublist-or-empty
  [sublist x]
  (conj (or sublist []) x))

(sublist-or-empty false 1) ; will conj [] with 1
(sublist-or-empty [2] 1) ; will conj the sublist with 1

; same as:
(defn sublist-if-empty
  [sublist x]
  (if sublist ; or do an inline if (conj (if....) x)
    (conj sublist x)
    (conj [] x)))

(sublist-if-empty false 1)
(sublist-if-empty [2] 1)

;; not=
(not= 1 1 1 1 1 1) ; false

(not= 1 1 1 1 1 2) ; true

(apply not= '(1 1 1 1 1 1)) ; false

(let [a 1]
  (apply not= (list 2 2 2 2 2 2 a))) ; true

;; if-let
; assign let when truthy

; yes and age assigned
(if-let [age (:age data/person)]
  (str "yes - " age)
  "no")

; no and address not assigned
(if-let [address (:address data/person)]
  (str "yes - " address)
  "no")

;; cond
; use the cond macro when you have 3 or more if's/branches
(defn string-number
  [n]
  (cond
    (odd? n) "odd"
    ;; (even? n) "even"
    (< n 0) "negative"
    (> n 0) "positive"))

(string-number 1)
(string-number 2)
(string-number -1)
(string-number 0) ; returns nil when no match

;; condp
; uses a function to decide which branch to execute
(defn op 
  [sel]
  (condp = sel
    "plus"  +
    "minus" -
    "mult"  *
    "div"   /))

((op "mult") 3 3) ; (* 3 3)