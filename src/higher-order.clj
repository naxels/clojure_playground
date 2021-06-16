(ns higher-order
  (:require [clojure.test :refer [is]]
            data))

;; reduce
(is (= 15
       (reduce + [1 2 3 4 5]) ; simple actually reducing
       (reduce + 0 [1 2 3 4 5]) ; same, with initial value
       (apply + [1 2 3 4 5])))  ; same

; reduced - indicate to stop reducing and return result
(reduce-kv ; reduce-kv is easier for Map operations
 (fn [m k v]
   (if (> k 2) ; stop after key reaches 2
     (reduced m)
     (assoc m k v)))
 {}
 [:a :b :c :d :e])

(def repos [{:private true} {:private true} {:private false}])

; great example on reducing over a map, updating values for keys
(reduce-kv (fn [m _index repo]
             (let [key (if (:private repo)
                         :private
                         :public)]
               (update m key inc)))
           {:public 0
            :private 0}
           repos)

(defn count-occurrences [coll]
  (->> coll
       (map #(vector % 1)) ; turn each item into vector with a 1
       (reduce (fn [m [k cnt]]
                 (assoc m k ; add k to m with value of below calc
                        (+ cnt
                           (get m k 0) ; get k from m or 0
                           )))
               {} ; into map
               )))

(defn word-count [str]
  (count-occurrences (.split #"\s+" str)))
;   (frequencies (.split #"\s+" str))) ; Clojure standard lib

(is (= {"women" 1, "of" 1, "children" 1, "To" 1, "things," 1, "and" 1, "all" 3, "the" 1, "men," 1}
       (word-count "To all things, all men, all of the women and children")))

(reduce (fn [_m i] {i 1}) {} (range 10)) ; if you don't use the previous value, you will overwrite until done

; reductions - helps visualizing the reduce process as it returns the intermediate steps
(reductions + [1 2 3 4 5])
(reductions conj [] [1 2 3 4 5])
(reductions + (map #(* % %) (range 5))) ; from 0->5, double the number, add them up
(reduce + (map #(* % %) (range 5))) ; will return just the 30

; when writing a custom function for reduce, needs 2 arguments
; 1st: the accumulator up until that point
; 2nd: the value coming from the collection at that iteration
(reduce (fn [so-far val]
          (assoc so-far val (count so-far)))
        {}
        [:a :b :c])

;; apply ; applies all items in coll as argument
(is (= "string1string2bla" (apply str data/strings-vec)))

; apply is useful here to filter b nil out
(let [a 5
      b nil
      c 18]
  (apply + (filter integer? [a b c])))

; min and max error out on empty coll, so supply 0 before hand
(apply min 0 [])

; vs map which does operation on each + returns list
(is (= '("string1" "string2" "bla") (map str data/strings-vec)))

;; map
(map #(* % 2) (range 10))

(map odd? (range 10))

(map inc (range 10)) ; (1 2 3 4 5 6 7 8 9 10)

; a set #{} is a filter and can be used as predicate:
(#{:foo :bar} :foo)
(#{:foo :bar} :foo2)
(map #{"bla"} data/strings-vec) ; (nil nil "bla")

; a map can be used as a function
;; visit-counts is a map
(def visit-counts {"ethel" 11 "lucy" 26 "ricky" 5})
;; map used in function position
(visit-counts "ethel")
;; map passed as function to higher-order function
(map visit-counts ["ricky" "lucy"])

;; mapv - mapv is a specialized version of map producing a vector instead of a lazy-sequence as output. 
;         It uses a transient internally so it’s faster than the equivalent (into [] (map f coll))
; diff: https://clojuredocs.org/clojure.core/mapv#example-5dcd0c40e4b0ca44402ef7de

(mapv inc (range 10)) ; [1 2 3 4 5 6 7 8 9 10]

;; map-indexed ; f needs 2 args, puts an index number as 1st arg
(map-indexed (fn [idx v] [idx (inc v)]) (range 10))

;; filter
(filter even? (range 10)) ; (0 2 4 6 8)

(filter #(> % 5) (range 10))

(filter #(= (count %) 3) data/strings-vec) ; only when length of word is 3

(is (= '("bla") (filter #{"bla"} data/strings-vec))) ; only returns the found value

;; filterv - returns a vector rather than a sequence.
(filterv even? (range 10)) ; [0 2 4 6 8]

;; example of a function taking function as argument
(defn total_price
  [price fee]
  (+ price (fee price)))

(defn flat_fee
  [_price]
  5)

(defn proportional_fee
  [price]
  (* price 0.12))

(total_price 1000 flat_fee)
(total_price 1000 proportional_fee)

;; another example adjusted from Functional Programming PragPub Ch5: Creating a Higher-Order Function in Scala
(def prices '(10 20 15 30 45 25 82))

(defn total-prices
  ([coll] (total-prices coll (constantly true))) ; tip from Discord: call func and set pred to always true
  ([coll pred]
   (let [f-applied (filter pred coll)]
     (reduce + 0 f-applied))))

(total-prices prices)

(total-prices prices #(> % 40))

(total-prices prices #(< % 40))

; ability to send in the function as well as predicate
(defn total-prices-fn
  [coll f pred]
  (let [f-applied (f pred coll)]
    (reduce + 0 f-applied)))

(total-prices-fn prices filter #(> % 40))

(total-prices-fn prices filter #(< % 40))

(total-prices-fn prices map #(* 2 %))

(total-prices-fn prices take 3)

; given by person from Discord discussion about the above total-prices functions
(defn total-prices-transduce
  [f coll]
  (transduce f + coll))

(total-prices-transduce (filter #(> % 40)) prices)

(total-prices-transduce (filter #(< % 40)) prices)

(total-prices-transduce (map #(* 2 %)) prices)

(total-prices-transduce (take 3) prices)