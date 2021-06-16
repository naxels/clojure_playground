(ns misc
  (:require data))

;; < , <= , > , >= . These functions return true when their arguments are in the correct order, so you can stack them
; from The Clojure Workshop
(let [x 50]
  (if (or (<= 1 x 100) ; <- very interesting, much more readable than javascript: if (x >= 1 && x <= 100)
          (zero? (mod x 100)))
    (println "Valid")
    (println "Invalid")))

(let [a 1
      x 50
      y 99
      b 100
      z 150]
  (<= 1 a x y 100 b z)) ; true

;; identity - returns what you pass to it
(identity 4) ; 4

; can be very handy when you do an operation on coll and also want the coll back
(map (juxt :name identity) data/customers)