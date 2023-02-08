(ns puzzles.prime)

; from: https://clojure-diary.gitlab.io/2022/10/02/finding-primes-from-1-to-100-in-clojure.html

(defn prime? [number]
  (every? false?
          (map #(= (mod number %) 0) (range 2 number))))

; SOLUTION TO PRIME FOR 1-100
(defn return-number-if-prime [number]
  (when (prime? number) number))

(remove nil?
        (map #(return-number-if-prime %) (range 1 101)))
; SOLUTION TO PRIME FOR 1-100

; my solution which is radically more simple:
(filter prime? (range 1 101))

; result is the same
(= (remove nil?
           (map #(return-number-if-prime %) (range 1 101)))
   (filter prime? (range 1 101)))

; my solution is MUCH faster
(time (remove nil?
              (map #(return-number-if-prime %) (range 1 101))))

(time (filter prime? (range 1 101)))