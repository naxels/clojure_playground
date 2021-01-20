(ns debug
  (:require [data]
            [clojure.inspector :refer [inspect-tree]]))

; use prn
(defn foo-prn [x]
  (let [y (+ x 1)]
    (prn y)
    y))

(foo-prn 10)

; use println - for human readable
(defn foo-println [x]
  (let [y (+ x 1)]
    (println y)
    y))

(foo-println 10)

; (inspect-tree document)
(defn foo-inspect [x]
  (let [y (+ x 1)]
    (inspect-tree y)
    y))

(foo-inspect 10)

; (comment ...)
(comment
  (first data/user-info))

; (def name value) within a function
(defn foo-inlinedef [x]
  (let [y (+ x 1)]
    (def yy y)
    y))

(foo-inlinedef 10)
yy

; tap>
; https://clojure.org/reference/repl_and_main#_tap
; https://quanttype.net/posts/2018-10-18-how-i-use-tap.html

; add function to tap for output
; Bound to current output (Calva)
; (add-tap (bound-fn* clojure.pprint/pprint))
; (add-tap (bound-fn* prn))
; Output to the terminal
; (add-tap prn)
; (add-tap clojure.pprint/pprint)
; (add-tap inspect-tree)
; make sure to add at least 1!

; tap> outputs to all added taps
(tap> "hello world")
(tap> data/customers)
(tap> data/hospital)

(defn foo-tap [x]
  (let [y (+ x 1)]
    (tap> y)
    y))

(foo-tap 10)

; remove taps
; (remove-tap (bound-fn* prn))
; (remove-tap (bound-fn* clojure.pprint/pprint))
; (remove-tap prn)
; (remove-tap clojure.pprint/pprint)
; (remove-tap inspect-tree)