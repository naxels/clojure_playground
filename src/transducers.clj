(ns transducers)

; https://clojure.org/reference/transducers

; my take: allows for reuse of composed function(s)
; basically: define once, reuse everywhere
; handy is: the composed function doesn't care about input or output
; afterwards you can do a variety of things with the result of transduction

; composed function:
(def inc-odd
  (comp
   (map inc)
   (filter odd?)))

; transduce - will immediately (not lazily) reduce over coll
(time (transduce inc-odd + (range 10000)))

; equivalent to (but without composing):
(time
 (->> (range 10000)
      (map inc)
      (filter odd?)
      (reduce +)))

; with init val
(transduce inc-odd + -250000 (range 1000))

; using into
(into [] inc-odd (range 10))

; sequence - lazy transducing
(sequence inc-odd (range 10))

; completing - make a transduce compatible fn
(transduce (map inc)
           (completing (fn [[sum cnt] x] [(+ sum x) (inc cnt)]) ; f
                       (fn [[sum cnt]] (/ sum cnt)))            ; cf
           [0 0]
           (range 7))