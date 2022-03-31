(ns comparison-and-equality
  (:require [clojure.test :refer [is]]))

;; compare - returns -1,0,1

; example with ZonedDateTime comparison, handy for use in filters
(let [date1 (java.time.ZonedDateTime/parse "2020-01-01T00:00:00Z")
      date2 (java.time.ZonedDateTime/parse "2021-01-01T00:00:00Z")]
  (is (= -1 (compare date1 date2)))
  (is (= 0 (compare date1 date1)))
  (is (= 1 (compare date2 date1))))

;; <
(is (= true (< 1 5)))
(is (= false (< 5 1)))

(is (= true (< 1 5 10)))
(is (= false (< 1 5 4)))

;; >
(is (= false (> 1 5)))
(is (= true (> 5 1)))

(is (= false (> 1 5 10)))
(is (= true (> 5 4 1)))