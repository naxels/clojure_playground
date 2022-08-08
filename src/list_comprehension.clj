(ns list-comprehension)

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

; For each x, generate all y's and z's
(for [x ['a 'b 'c]
      y [1 2 3]
      z (range 2)]
  [x y z])

; For each first number generate all other numbers and so forth
(count (for [a1 (range 10)
             a2 (range 10)
             a3 (range 10)
             a4 (range 10)
             a5 (range 10)]
         (+ a1 a2 a3 a4 a5))) ; 10 * 10 * 10 * 10 * 10 = 100000

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

; :while in between x and y
(for [x (range 3)
      :while (not= x 1) ; this will cause the while to stop on x as soon as x turns to 1
      y (range 3)]
  [x y])

; vs this will skip x = 1 but show x = 2
(for [x (range 3)
      y (range 3)
      :while (not= x 1)]
  [x y])

; inline multiple :while
(for [x (range) :while (< x 10)
      y (range) :while (<= y x)]
  [x y])

; destruct
(for [[_k v] {:a 1 :b 2 :c 3}]
  v)

; Easy iteration through nested vectors
(for [row [["a" "b"] ["c" "d"]]
      letter row]
  letter)

; vs mapcat example:
(for [i (range 1 14)
      a ["D" "C" "H" "S"]
      :let [card (str i "-" a)]]
  card)

(mapcat (fn [i] 
          (map (fn [a] (str i "-" a)) 
               ["D" "C" "H" "S"])) 
        (range 1 14))

; easy indexed generator
(for [[i item] (map-indexed vector '(aap noot mies))]
  [:li {:key i} item])