(ns operations-on-collections
  (:require [clojure.test :refer [is]]
            [custom-handy-fns :refer [associate-by]]
            data))

;; get
(is (= 15 (get data/coll-vec 10))) ; get's the 10th value

; implied get:
(is (= 1 (:a data/coll-map)))

; get can return a different value for not found
(is (= "Not Found" (get data/coll-map :z "Not Found")))

; (some? x) is equal to (not (nil? x))

;; some ; boolean test, Returns the first logical true value
; returns the value of the set that you are looking for
;; https://stackoverflow.com/a/32405094 ; benchmark of func with some with reduce
(is (= 15 (some #{15} data/coll-vec))) ; https://insideclojure.org/2015/01/27/clojure-tips-contains/
(is (nil? (some #{21} data/coll-vec))) ; and returns nil when not found

; getting the first matching value
;; https://stackoverflow.com/a/10192733/ ; due to lazy, defn find-first will not process entire coll
(defn find-first
  [f coll]
  (first (filter f coll)))

(is (= 6 (find-first #(> % 5) data/coll-vec)))
; vs, some
(is (= 6 (some #(when (> % 5) %) data/coll-vec)))
; vs, reduce is faster they say on larger infinite colls
(is (= 6 (reduce #(when (> %2 5) (reduced %2)) nil data/coll-vec)))

(is (= true (some #(= 15 %) data/coll-vec)))
(is (nil? (some #(= 14 %) data/coll-vec))) ; there is no 14th key/index

; can also return true/false
(is (= true (some even? data/coll-vec)))
; you can change a value to boolean quickly with some?
(is (= false (some? (some #(when (> % 30) %) data/coll-vec))))
; or
(is (= false (boolean (some #(when (> % 30) %) data/coll-vec)))) ; Everything except false and nil is logically true in Clojure
; or
(is (= false (not (not-any? #(when (> % 30) %) data/coll-vec)))) ; is not any? !

; https://stackoverflow.com/questions/9491400/why-does-clojure-not-have-an-any-or-any-pred-function

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

;; every? true if all fit the predicate
(is (= true (every? keyword? (keys data/coll-map))))

(is (= true (every? #(< % 90) data/coll-vec)))
(is (= false (every? #(> % 5) data/coll-vec)))

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

(assoc nil :a :b) ; nil is seen as empty map

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

;; dissoc remove key(s) from map
(dissoc data/coll-map :z) ; not found key just returns the coll
(dissoc data/coll-map :a)

; dissoc can be of great use as an opposite to select-keys if there are more keys to keep than remove from map
(dissoc data/person :house)
; same as:
(select-keys data/person [:name :age :gender :bike])

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

;; conj ; add value to the most efficient position of the coll
; vec: at the end
(conj [1 2 3] 4)
; list: at the beginning
(conj '(1 2 3) 4)
; map: at the end can be done with map and vec length of 2
(is (= (conj data/coll-map-words {"jaap" 1})
       (conj data/coll-map-words ["jaap" 1]))) ; same because it can be converted to map

;; cons ; Returns a new seq where x is the first element and seq is the rest. So list: add value to beginning of list
(cons 1 '(2 3 4 5))
(cons 1 [2 3 4 5])

;; concat
(is (= (into [] (concat [1 2 3] [4 5 6])) ; vector
       (concat [1 2 3] [4 5 6]))) ; list

(concat [:a :b] nil [1 [2 3] 4]) ; nil is not added

(concat [1 2] [3 4])

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

;; split-at
(split-at 2 data/coll-vec)
(split-at 100 data/coll-vec) ; doesn't error on our of range

;; split-with
(split-with (partial >= 3) data/coll-vec)
(split-with #(< (mod % 6) 5) data/coll-vec)
; easily testible by just running the function with map
; (map #(< (mod % 6) 5) data/coll-vec)

; remove the first occurance from coll:
(let [[n m]
      (split-with (partial not= 5) data/list-two)]
  (concat n (rest m))) ; concat the coll, skipping the first of m

;; transpose
(defn transpose
  [coll]
  (apply mapv vector coll) ; [[1 4 7] [2 5 8] [3 6 9]]
  #_(apply map vector coll))   ; ([1 4 7] [2 5 8] [3 6 9])

(transpose [[1 2 3] [4 5 6] [7 8 9]])
; same as
(mapv vector [1 2 3] [4 5 6] [7 8 9])

;; sort-by
; sorting using comp
(sort-by (comp - :created_at) data/unordered)
; same as
(sort-by #(- (:created_at %)) data/unordered)

; can also be done with a comparator:
(defn descending
  "sort descending"
  [a b]
  (compare b a))

(sort-by :created_at descending data/unordered)

; this doesn't work
;; (sort-by :created_at - unordered) ; since - is not used properly as comparator

;; every-pred - combine predicates
(filter (every-pred #(> % 5)
                    #(< % 10)
                    even?)
        data/coll-vec)

; can also do with applying every-pred to for example vec:
(def filters [#(> % 5)
              #(< % 10)
              even?])

(filter (apply every-pred filters) data/coll-vec)

;; subvec
(subvec [1 2 3 4] 1 3) ; [2 3]

; create an index function and return index or -1
; https://programming-idioms.org/idiom/222/find-first-index-of-an-element-in-list/4495/clojure
(defn find-index [x items]
  (or (->> items
           (map-indexed vector)
           (filter #(= x (peek %)))
           ffirst)
      -1))

(find-index 9 (range 10))
(find-index 11 (range 10))

;; keep Returns a lazy sequence of the non-nil results of (f item). Note this means false return values will be included.
(keep even? (range 1 10))

; great example and comparison:
; https://clojuredocs.org/clojure.core/keep#example-542692cfc026201cdc326e82

(filter odd? (range 10)) ; returns the vals
(keep odd? (range 10)) ; returns the output of odd?
(map odd? (range 10)) ; same as keep in this case

(keep {:a 1, :b 2, :c 3} [:a :b :d]) ; returns the map values found in the coll

;; distinct - remove all duplicates
(distinct data/list-two)

;; dedup - remove consecutive duplicates
(dedupe data/list-two)

;; min-key
;; max-key
(min-key peek ["Jon" 1] ["Rich" 6] ["Nancy" 3])
(max-key peek ["Jon" 1] ["Rich" 6] ["Nancy" 3])
(max-key count "asd" "bsd" "dsd" "long word")
(apply max-key val {:a 3 :b 7 :c 9})

;; merge - merge with the latest value for duplicates
(merge
 {:flintstone, ["Fred"], :rubble ["Barney"]}
 {:flintstone, ["Wilma"], :rubble ["Betty"]}
 {:flintstone, ["Pebbles"], :rubble ["Bam-Bam"]})

(merge
 {:a 1 :b 1}
 {:a 2 :b 2}
 {:a 3 :b 3})

;; merge-with - merge with a function for how to handle duplicates
(merge-with concat
            {:flintstone, ["Fred"], :rubble ["Barney"]}
            {:flintstone, ["Wilma"], :rubble ["Betty"]}
            {:flintstone, ["Pebbles"], :rubble ["Bam-Bam"]})

(merge-with +
            {:a 1 :b 1}
            {:a 2 :b 2}
            {:a 3 :b 3}
            {:b 1 :c 1})

(merge-with into
            {"Lisp" ["Common Lisp" "Clojure"]
             "ML" ["Caml" "Objective Caml"]}
            {"Lisp" ["Scheme"]
             "ML" ["Standard ML"]})

; this won't work: it conj's the entire vec into the original vec, creating a subvec
(merge-with conj
            {"Lisp" ["Common Lisp" "Clojure"]
             "ML" ["Caml" "Objective Caml"]}
            {"Lisp" ["Scheme"]
             "ML" ["Standard ML"]})

; using an apply when working on coll of maps:
(apply merge-with concat
       '({:flintstone, ["Fred"], :rubble ["Barney"]}
         {:flintstone, ["Wilma"], :rubble ["Betty"]}
         {:flintstone, ["Pebbles"], :rubble ["Bam-Bam"]}))

;; group-by - combine vals by key, always puts vals in vector
(group-by :name data/persons)

(group-by (fn [i]
            (mod i 3))
          (range 20))

;; zipmap - takes keys and vals and puts them together in a map
(zipmap (map :name data/persons) data/persons)

(zipmap [:a :b :c :d :e] [1 2 3 4 5])

; will stop when keys run out before vals
(zipmap [:a :b :c] [1 2 3 4])

;; associate-by - same as group-by but use for 1-1 relationship, this will put key per val, no subcoll 
(associate-by :name data/persons)

;; find - Returns the map entry for key, or nil if key not present.
(find data/people "Carl")
(find data/people "Peter")

;; key - Returns the key of the map entry.
(key (find data/people "Carl"))

;; val - Returns the value in the map entry.
(val (find data/people "Carl"))

;; butlast
(butlast data/people)
(butlast data/list-one)
(butlast '()) ; nil
(butlast '(1)) ; nil

;; interleave - Returns a lazy seq of the first item in each coll, then the second etc.
; lazy seq of :fruit "grape" etc
(interleave [:fruit :color :temp]
            ["grape" "red" "hot"])

; turn into map
(apply assoc {}
       (interleave [:fruit :color :temp]
                   ["grape" "red" "hot"]))

;; interpose - Returns a lazy seq of the elements of coll separated by sep.
(interpose ", " ["one" "two" "three"])

; turn to string:
(apply str (interpose ", " ["one" "two" "three"]))

; alternatively use clojure.string/join without interpose

; example usage:
; - create csv with ,
; - create markdown table row with \|

;; not-any?
(not-any? odd? data/test-coll)

(not-any? int? data/candy)
(not-any? int? data/user-info)

(not-any?   #(= "query"  %)   (list "hola" "query" "fin"))   ;false
(not-any?   #(= "query"  %)   (list "hola" "fin"))           ;true
(not-any?   #(= "query"  %)   (list))                        ;true
(not-any?   #(= "query"  %)   nil)                           ;true
(not-any?   #(= "query"  %)   '())                           ;true

; alternative that can be faster:
; from https://narimiran.github.io//2023/01/17/advent-of-clojure.html

(defn none?
  [pred xs]
  (reduce (fn [acc x]
            (if (pred x)
              (reduced false) ; stops reduce and returns false
              acc))
          true
          xs))

(none? odd? data/test-coll)

(none? int? data/candy)
(none? int? data/user-info)

(none?   #(= "query"  %)   (list "hola" "query" "fin"))   ;false
(none?   #(= "query"  %)   (list "hola" "fin"))           ;true
(none?   #(= "query"  %)   (list))                        ;true
(none?   #(= "query"  %)   nil)                           ;true
(none?   #(= "query"  %)   '())                           ;true