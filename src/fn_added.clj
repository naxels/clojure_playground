(ns fn-added)

;; for all core fn's show the fn & when it's added, sorted by fn, name

(ns-map 'clojure.core)

(def ns-fns (keys (ns-publics 'clojure.core)))

(defn get-added
  [fn-name]
  (:added (meta (ns-resolve 'clojure.core (symbol fn-name)))))

(defn fn-ver
  [acc fn-name]
  (let [added (get-added fn-name)]
    (if added
      (assoc acc fn-name added)
      acc)))

(def fn-and-ver (reduce fn-ver {} ns-fns))

(def grouped-ver (group-by val fn-and-ver))

(def grouped-ver-keep-fn (update-vals grouped-ver #(map first %)))

(keys grouped-ver-keep-fn)

(def grouped-ver-counted (map (fn [[ver vals]] [ver (count vals)]) grouped-ver-keep-fn))

(sort-by second grouped-ver-counted)

(sort (get grouped-ver-keep-fn "1.11"))

; for some reason it's hard to sort, parsing 1.1, 1.10 and 1.11 string, then sorting correctly

;; (sort-by #(Float/parseFloat (val %)) > fn-and-ver)

;; (sort-by #(BigDecimal. (val %)) > fn-and-ver)

;; (Float/parseFloat "1.11")

;; (.doubleValue (Float/parseFloat "1.10"))

;; (Double/parseDouble "1.10")

;; (>
;;  (BigDecimal. "1.1")
;;  (BigDecimal. "1.10")
;;  (BigDecimal. "1.11"))

;; (< (BigDecimal. "1.1")
;;    (BigDecimal. "1.10"))

;; (parse-double "1.10")
;; (parse-double "1.11")