(ns conditionals)

;; Very interesting because you can 'stack' the function calls like with + / - * etc

;; and
(let [a true
      b false]
  (and a b)
  
  ; equivalent of
  (if (= a true)
    (if (= b true)
      true
      false)
    false))

;; or
(def enemy {:name "Zulkaz", :health 250, :armor 0.8, :camp :trolls})

(let [target enemy]
  (or (:armor target) 0)

; equivalent of
  (if (:armor target) ; testing if :armor exists
    (:armor target) ; if true, return that value
    0) ; else return 0
  )

;; =
(let [a 0
      b 0]
  (if (= a b 1) 0 1) ; return 0 when any of a or b is 1

  ; equivalent of
  (if (or (= a 1)
          (= b 1))
    0
    1))