(ns functional-programming-patterns.functional-patterns.tail-recursion
  (:require [data]))

; define/loop it all yourself
(defn make-people
  [first-names last-names]
  (loop [first-names first-names
         last-names last-names
         people []]
    (if (seq first-names)
      (recur
       (rest first-names)
       (rest last-names)
       (conj people
             {:first (first first-names) :last (first last-names)}))
      people)))

(make-people data/firstnames data/lastnames)

; more function driven with core fn's
(defn shorter-make-people
  [first-names last-names]
  (for [[first last] (partition 2 (interleave first-names last-names))]
    {:first first :last last}))

(shorter-make-people data/firstnames data/lastnames)

; idea: turn colls of first & last names into vec of maps {:first .. :last ..}

; doesn't work, creates map of first+lastnames, but not in maps themselves
(zipmap data/firstnames data/lastnames)

; simplest solution I thought of:
(map (fn [first last] {:first first :last last}) data/firstnames data/lastnames)