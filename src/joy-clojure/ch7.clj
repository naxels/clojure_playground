(ns joy-clojure.ch7)

(def plays [{:band "Aap",        :plays 1500, :loved 50}
            {:band "Aap",        :plays 500,  :loved 100}
            {:band "Burial",     :plays 979,  :loved 9}
            {:band "Eno",        :plays 2333, :loved 15}
            {:band "Bill Evans", :plays 979,  :loved 9}
            {:band "Magma",      :plays 2665, :loved 31}])

; sort by 1 column, just use the keyword
(sort-by :band plays)

; closure function for multiple columns
(defn columns [column-names]
  (fn [row]
    (vec (map row column-names))))

((columns [:plays :band]) {:band "Aap" :plays 1500 :loved 50}) ; [1500 "Aap"]

; how this works, is dynamic:
(defn col-plays-band
  [row]
  (vec (map row [:plays :band])))

; sort on multiple keys using columns fn
(sort-by (columns [:plays :band]) plays)
(sort-by (columns '(:plays :band)) plays)
(sort-by col-plays-band plays)

; same as: (however have to manually add each column)
(sort-by (juxt :plays :band) plays)

; dynamically: get all keys & order a-z:
(def all-keys (sort (keys (first plays))))

(sort-by (columns all-keys) plays)