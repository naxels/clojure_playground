(ns list-comprehension
  (:require #_[clojure.test :refer [is]]))

;; for
;; 
;; Worth noting: 
;; Those coming from the imperative camp may look to for to achieve side-effects. 
;; That won’t work well, since Clojure’s for is !lazy!; 
;; if it’s not consumed, it’ll never be realized. 
;; It may also only be partially consumed. 
;; > Instead, consider doseq.
;; > Most of the time, using map or filter will be not only more clear, 
;; but also more concise. 
;; > If you want early termination, however, 
;; > or nested iterations, it’s worthwhile to know the semantics of Clojure’s for.

(for [x ['a 'b 'c]
      y [1 2 3]]
  [x y])

(for [x1 [1 2 3]
      x2 [1 2 3]]
  (* x1 x2))

(for [x (range 6)]
  (* x x))

; when ; is filtering
(for [x [0 1 2 3 4 5]
      :let [y (* x 3)]
      :when (even? y)]
  y)

; https://clojuredocs.org/clojure.core/for#example-542692d3c026201cdc326fa7
(for [x (range 3)
      y (range 3)
      :when (not= x y)]
  [x y])

(for [x {:a 1 "b" 2 :c 3}
      :when (-> x first keyword?)]
  x)

; while ; is stopping when encountered
; https://clojuredocs.org/clojure.core/for#example-542692d3c026201cdc326fa7
; > the reason it gives ([1 0] [2 0] [2 1])
; is because it stops at the first sign of the condition, so:
; 0 0 = stop !
; 1 0 = good
; 1 1 = stop !
; 2 0 = good
; 2 1 = good
; 2 2 = stop !
(for [x (range 3)
      y (range 3)
      :while (not= x y)]
  [x y])

(for [x (range 4)
      y (range 4)
      :while (not= x y)]
  [x y])

(for [x (range 3)
      :while (not= x 1) ; this will cause the while to stop on x and not even consider y
      y (range 3)]
  [x y])

; destruct
(for [[_k v] {:a 1 :b 2 :c 3}]
  v)