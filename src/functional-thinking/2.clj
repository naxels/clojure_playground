(ns functional-thinking.2)

(defn classify [num]
  (let [factors (->> (range 1 (inc num))
                     (filter #(zero? (rem num %))))
        sum (reduce + factors)
        aliquot-sum (- sum num)]
    (cond
      (= aliquot-sum num) :perfect
      (> aliquot-sum num) :abundant
      (< aliquot-sum num) :deficient)))

(map classify (range 1 100))

(->> (range 1 10000)
     (map classify)
     (filter #(= :perfect %)))
    ;;  count)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; 

(defn perfect-num-vec [nr]
  [nr (classify nr)])

(map #(perfect-num-vec %) (range 1 1000))

;; https://en.wikipedia.org/wiki/Perfect_number#Odd_perfect_numbers

(defn perfect-numbers
  [until-nr]
  (->> (range 1 until-nr)
       (filter even?)
       (map #(perfect-num-vec %))
       (filter #(= :perfect (peek %)))))

(perfect-numbers 10000)
; 10000
;; [6 :perfect] [28 :perfect] [496 :perfect] [8128 :perfect])

(def numbers (range 1 11))
(filter #(zero? (rem % 3)) numbers)

(def words ["the" "quick" "brown" "fox" "jumped" "over" "the" "lazy" "dog"])
(filter #(= 3 (count %)) words)

; get first result
;; (some #(= 3 (count %)) words) ; returns true/false
(some #(when (= 3 (count %)) %) words) ; returns value
(first (filter #(= 3 (count %)) words)) ; returns value
(take 1 (filter #(= 3 (count %)) words)) ; returns in col

(reduce + (range 1 11))
(apply + (range 1 11))

; partials
(def min-hundred (partial - 100))

(def half (min-hundred 50))

half