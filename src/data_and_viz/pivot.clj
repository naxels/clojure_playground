(ns data-and-viz.pivot 
  (:require [data]))

(defn pivot [data]
  (reduce-kv
   (fn [acc k v]
     (reduce (fn [acc' k'] (assoc-in acc' [k' k] (k' v)))
             acc
             (keys v)))
   {}
   data))

; from
; {1 {:good [1 2] :bad [3 4]}
;  2 {:good [5 6] :bad [7 8]}}
; to
; {:good {1 [1 2], 2 [5 6]}, 
;  :bad {1 [3 4], 2 [7 8]}}
(pivot data/map-of-map-vec)

; other interesting way is Reshape:
; https://scicloj.github.io/tablecloth/index.html#Reshape
;; pivot->longer
;; pivot->wider