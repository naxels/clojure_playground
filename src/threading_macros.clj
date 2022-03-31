(ns threading-macros
  (:require [data]))

;; as-> ; assign value to variable, then put it anywhere in next forms
(as-> 0 n ; sets 0 to n
  (inc n)
  (inc n)
  (+ n 5))

;; cond-> (thread first) && cond->> (thread last)
; walks through all expressions, ignoring expressions that evaluate to false
; example from brave clojure 2021-03-31 meeting: take a property, select-keys (true) then only if rental do rental function, then only if commercial, do commercial function.
(defn correct-date
  [article]
  (cond-> article
    (= (:date article) "2017-11-23") (assoc :date "2021-04-01") ;<- change date if this date
    (= (:author article) "Emma Cribs") (assoc :author "Emma House"))) ; <- change author if this author 

(map correct-date data/articles)

(cond-> 1
  true inc ; executes because true
  false (* 42) ; doesn't because false
  :always (- 2)) ; is truthy as well
; => 0

;; some-> (thread first) && some->> (thread last)
; return immediately if any of the forms evaluates to nil
; this example would otherwise return a NullPointerException
(some->
 {:a 1 :b 2}
 :c inc)

; this example, just putting -> would result in NumberFormatException
(or (some->
     (System/getenv "PORT") Integer.)
    4444)