(ns debug
  (:require [data]
            [clojure.inspector :refer [inspect-tree]]
            [clojure.tools.trace :as trace]))

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
(add-tap prn)

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
(remove-tap prn)

; debug inside anonymous fn:
(#(inc %) 1) ; this won't show before data

(#(inc (doto % println)) 1) ; this shows before and after
; using do:
(#(do
    (println %)
    (inc %))
 1)

(defn dbg
  "print input & return input"
  [data]
  (println data)
  data)

(def dbg2 #(doto % println))

; debug intermediate steps in threading:
(-> data/hospital
    (:staff)
    (keys)
    (doto tap>) ; with data as first param, this works
    (dbg) ; works
    (dbg2) ; works
    #_(println) ; won't work because return is nil
    (doto println) ; this now works, again data as first param
    (first))

(->> data/hospital
     (:staff)
     (keys)
     #_(println) ; this won't work as println returns nil
     #_#(doto % println) ; this doesn't work either
     #_(tap>) ; this doesn't work either as it won't return the data, but boolean
     (#(doto % println)) ; with double parens & anonymous fn, it works
     (#(doto % tap>)) ; works as well
     (dbg) ; works
     (dbg2) ; works
     (first))

; using tracing
(trace/trace "data" ; providing a name
 (-> data/hospital
     (:staff)
     (keys)
     (first)))

; trace a var or fn:
(trace/trace-vars +)

(apply + (range 5))

; untrace again
(trace/untrace-vars +)

; using a deftrace fn
(trace/deftrace idntty [x] x)

(-> data/hospital
    (idntty)
    (:staff)
    (idntty)
    (keys)
    (idntty)
    (first))

; trace within threading
(-> data/hospital
    (:staff)
    (keys)
    (trace/trace) ; without name
    #_(trace/trace "keys") ; won't work!
    (#(trace/trace "keys" %)) ; this works with double parens
    #_#(trace/trace "keys" %) ; won't work
    (first))

(->> data/hospital
     (:staff)
     (keys)
     (trace/trace) ; without name
     (trace/trace "keys") ; with name
     (#(trace/trace "keys" %)) ; as anon fn with name
     (first))