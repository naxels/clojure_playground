(ns higher-order
  (:require [clojure.test :refer [is]]))

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

(def repos [{:private true}{:private true}{:private false}])

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

;; apply ; applies all items in coll as argument
(def strings-vec ["string1" "string2" "bla"])

(is (= "string1string2bla" (apply str strings-vec)))

; apply is useful here to filter b nil out
(let [a 5
      b nil
      c 18]
  (apply + (filter integer? [a b c])))

; min and max error out on empty coll, so supply 0 before hand
(apply min 0 [])

; vs map which does operation on each + returns list
(is (= '("string1" "string2" "bla") (map str strings-vec)))

;; map
(map #(* % 2) (range 10))

(map odd? (range 10))

(map inc (range 10))

; a set #{} is a filter and can be used as predicate:
(#{:foo :bar} :foo)
(#{:foo :bar} :foo2)
(map #{"bla"} strings-vec) ; (nil nil "bla")

;; filter
(filter even? (range 10))

(filter #(> % 5) (range 10))

(filter #(= (count %) 3) strings-vec) ; only when length of word is 3

(is (= '("bla") (filter #{"bla"} strings-vec))) ; only returns the found value