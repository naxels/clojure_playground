; list functions of namespace:
(dir clojure.string)

; ignore expression
#_(expression)

; truthy and falsy
nil
false
; all else will return true in case of if etc

; an if can usually be replaced with an (and) or (or) due to truthy/falsy
(if (:armor target) ; testing if :armor exists
    (:armor target) ; if true, return that value
    0) ; else return 0

; easy replace by:
(or (:armor target) 0)

; most functions are lazy, therefor there is no function that returns the first result of predicate since it's the same as (first (filter ))

; filter returns seq, select returns set

; see/treat functions as values

; prefer zero? over = 0
(zero? (expression))
(= 0 (expression))

; any? seems to return just true for anything..
; so it's not the same as (not (not-any?)) !
; https://stackoverflow.com/a/39134040