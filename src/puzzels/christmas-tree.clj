(ns puzzels.christmas-tree)

(defn calculate-tree
  [levels start-width decrease]
  (loop [level levels
         x start-width
         level-map {}]
    (if (= level 0)
      level-map
      (recur (- level 1) 
             (- x decrease) 
             (assoc level-map level x)))))

(def tree-map (calculate-tree 6 80 14))

tree-map

(println (reverse (map println-str tree-map)))

; (defn print-tree
;   [[level width]]
;   level)

; (map print-tree (seq tree-map))

(reduce + (vals tree-map))