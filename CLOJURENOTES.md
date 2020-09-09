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