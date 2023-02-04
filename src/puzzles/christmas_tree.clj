(ns puzzels.christmas-tree)

; loop
(defn calculate-tree
  "not very smart way: returns a map with key level, value length
   will return 0 and negative for length"
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

; sum the values
(apply + (vals tree-map))

(defn calculate-tree-reduce
  "Return a list containing vectors of level, length
   will return 0 and negative for length"
  [levels start-width decrease]
  (reduce (fn [coll x] ; reductions
                (conj coll [x (- start-width (* x decrease))]))
              '() 
              (range levels)))

(def tree-map-reduce (calculate-tree-reduce 6 80 14))
tree-map-reduce

(apply + (map second tree-map-reduce))

; for
(defn calculate-tree-for
  "generates a list containing vectors of level length
   only returns positive y"
  [levels start-width decrease]
  (for [x (range levels)
        :let [y (- start-width (* x decrease))]
        :when (pos? y)]
    [x y]))

(def tree-map-for (calculate-tree-for 6 80 14))
tree-map-for

; iterate, only generating the widths
(defn calculate-tree-iterate
  "returns a list with the width results"
  [levels start-width decrease]
  (take levels (iterate #(- % decrease) start-width)))

(def tree-list-iterate (calculate-tree-iterate 6 80 14))
tree-list-iterate

(defn calculate-tree-iterate-pos
  "while iterate returns a positive result, returns the width results"
  [start-width decrease]
  (take-while pos? (iterate #(- % decrease) start-width)))

(def tree-list-iterate-pos (calculate-tree-iterate-pos 80 14))

(count tree-list-iterate-pos) ; length

; zipmap, generate 2 coll's from levels and iterate, then zipmap
(defn calculate-tree-zipmap
  [levels start-width decrease]
  (let [lvl-coll (range levels)
        calc-coll (iterate #(- % decrease) start-width)]
    (zipmap lvl-coll calc-coll)))

(def tree-map-zipmap (calculate-tree-zipmap 6 80 14))

; using reduce, destruct then sum
(reduce (fn [s [_lvl length]] (+ s length)) 0 tree-map-for) ; reductions

; get the lengths (destruct) in coll and sum them up
(apply + (map (fn [[_lvl length]] length) tree-map-for)) ; second value
(apply + (map second tree-map-for)) ; same
(apply + (map second tree-map-zipmap)) ; same

(apply + tree-list-iterate)
(apply + tree-list-iterate-pos)