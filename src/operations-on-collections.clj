(ns operations-on-collections
  (:require [clojure.test :refer [is]]
            data))

;; get
(is (= 15 (get data/coll-vec 10))) ; get's the 10th value

; implied get:
(is (= 1 (:a data/coll-map)))

; get can return a different value for not found
(is (= "Not Found" (get data/coll-map :z "Not Found")))

; (some? x) is equal to (not (nil? x))

;; some ; boolean test, Returns the first logical true value
(is (= true (some even? data/coll-vec)))

(is (= 15 (some #{15} data/coll-vec))) ; https://insideclojure.org/2015/01/27/clojure-tips-contains/

(is (= true (some #(= 15 %) data/coll-vec)))
(is (nil? (some #(= 14 %) data/coll-vec))) ; there is no 14th key/index

;; contains? ; warning: only for keys!
(is (= true (contains? data/coll-map :a)))

; NOTE!! not for vec's values, only keys!
(contains? data/coll-vec 15)
(is (= true (contains? data/coll-vec 1)))

;; empty?
(is (= false (empty? data/coll-vec)))
(is (= true (empty? [])))

; all are empty
(is (= true (every? empty? ["" [] () '() {} #{} nil])))

; recommended idiom (seq x) to test if coll is not empty:
;(not (empty? coll-map))
(is (= true (every? seq data/coll-map))) ; all except false and nil are true
; because seq on empty returns nil:
(is (nil? (seq [])))

;; every?
(is (= true (every? keyword? (keys data/coll-map))))

;; remove
; remove nil from coll
(is (= [1 2 3] (remove nil? [1 nil 2 nil 3 nil])))
; can also be done with filter and some
(is (= [1 2 3] (filter some? [1 nil 2 nil 3 nil]))) ; some will not return nil
; you can use sets to remove values
; also possible is to put the set in a (def #{})
(remove #{"one" "two"} ["one" "two" "three" "four"])

;; into
(into #{} (range 10))
(into [1 2 3] [4 5 6])
(is (= {:a 1 :b 2}
       (into {} [[:a 1] [:b 2]])
       (into {} [{:a 1} {:b 2}])))

; sort a map
(into (sorted-map) {:c 1 :a 1 :b 1})

; map turns data structures into seq, with an into (empty coll) you can turn it back to what it was:
(defn maintain-datastructure [f pred coll]
  (into (empty coll) (f pred coll)))

(->> #{1 2 3 4 5}
     (maintain-datastructure map inc)
     (maintain-datastructure filter odd?))

(->> {:a 1 :b 2 :c 5}
     (maintain-datastructure filter (comp odd? last)))

; without comp:
(->> {:a 1 :b 2 :c 5}
     (maintain-datastructure filter #(odd? (val %)))) ; can also use last, i think val makes it clear we want the value, not key

;; assoc
(assoc data/coll-map :z 6) ; new key
(assoc data/coll-map :b 22) ; update current key

(assoc [:a :b :c :d] 2 :z) ; replace vector index with val

(assoc [1 2 5 6 8 9] 2 100) ; another example of replace vector index with val

; for using with reduce:
(defn lookup [id] ; a function can easily return a map
  {:index "backup"
   :bucket (* 100 id)})

(reduce (fn [m item] (assoc m item (lookup item))) ; lookup is a new map
        {} ; into map
        [12 41 11])

;; assoc-in
(assoc-in data/coll-map-nested [:c :x1 :x2] "z2")

(assoc-in data/articles [2 :ads 1] 3) ; 2nd index, :ads key, 1st index, update to 3
;; (assoc-in articles [2 :ads 4] 3) ; index out of bounds

;; dissoc
(dissoc data/coll-map :z) ; not found key just returns the coll
(dissoc data/coll-map :a)

; combined:
(-> data/coll-map-nested
    (assoc :d 1)
    (dissoc :c)
    (update :d inc) ; update a key with a function
    (merge {:e 2})) ; merge with other associate data struct

;; update
(update data/coll-map :b inc)

(defn insert-word [w words]
  (update words w (fnil inc 0))) ; in case nil, inc 0 else inc the value

(insert-word "hello" data/coll-map-words)
(insert-word "bye" data/coll-map-words)

;; update-in
(update-in data/coll-map-nested [:c :x1 :x2] #(str % %)) ; duplicate the string value

;; conj
(is (= (conj data/coll-map-words {"jaap" 1})
       (conj data/coll-map-words ["jaap" 1]))) ; same because it can be converted to map

;; cons ; add value to beginning of list
(cons 1 '(2 3 4 5))
(cons 1 [2 3 4 5])

;; concat
(is (= (into [] (concat [1 2 3] [4 5 6])) ; vector
       (concat [1 2 3] [4 5 6]))) ; list

(concat [:a :b] nil [1 [2 3] 4]) ; nil is not added

;; take
(take 3 data/coll-vec)

;; take-while
; stops upon first false
(take-while #(> 10 %) [2 9 4 12 3 99 1])
(take-while #(> 10 %) data/coll-vec)

;; drop
(drop 3 data/coll-vec)

;; drop-while
; drops values until predicate is true, then returns rest
(drop-while #(> 10 %) [2 9 4 12 3 99 1])
(drop-while #(> 10 %) data/coll-vec)

; predicates using set #{} hash-set
(some #{:x :c} [:a :b :c :d :e]) ; :c

(remove #{:x :c} [:a :b :c :d :e]) ; (:a :b :d :e)

(filter #{:x :c} [:a :b :c :d :e]) ; (:c)

;; set on keyword filter example
(count (filter #((hash-set (:winner_name %) (:loser_name %)) "Roger Federer") data/match_scores))

; complex filter example that can be easily improved using hash-set:
; #(and
;   (or (= (:winner_name %) "Roger Federer")
;       (= (:winner_name %) "Rafael Nadal"))
;   (or (= (:loser_name %) "Roger Federer")
;       (= (:loser_name %) "Rafael Nadal")))
; much easier to read:
; #(= (hash-set (:winner_name %) (:loser_name %))
;     #{"Roger Federer" "Rafael Nadal"})
    
;; partition
(partition 1 data/coll-vec)
(partition 2 data/coll-vec)
(partition 2 1 data/coll-vec) ; put 2 in the 'bucket' and just step 1 forward
(partition 3 data/coll-vec) ; drops off values that don't make the 'bucket'

;; partition-all
; will put the values that don't make the normal 'bucket' in a as much as possible 'bucket'
(partition-all 3 data/coll-vec)

;; partition-by
; partition based on a function
(partition-by #(< 10 %) data/coll-vec)
(partition-by #(odd? %) data/coll-vec) ; it doesn't 'collect' values until pred is fully done
(partition-by #(> 5 %) data/coll-vec) ; easily displayed here
