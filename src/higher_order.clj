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

; easy way to create an indexed vector from your coll
(map-indexed vector (range 25))

;; mapcat
(mapcat reverse [[3 2 1 0] [6 5 4] [9 8 7]])

(mapcat identity [[1 2 3] [4 5 6]])

; fast way to get all vals in a map concat:
(mapcat vals data/persons)
; with map you would get a new seq for each entry
(map vals data/persons)

; mapcat example vs map vs apply concat
(mapcat reverse [[3 2 1 0] [6 5 4] [9 8 7]])
; vs just map
(map reverse [[3 2 1 0] [6 5 4] [9 8 7]])
; essentialy is doing this with map
(apply concat (map reverse [[3 2 1 0] [6 5 4] [9 8 7]]))

; @hiredman on Clojure Slack said: when I see a mapcat with a map in the fn, I think for!
(is (= (mapcat (fn [{:keys [categories] :as post}]
                 (map (fn [category] [category post]) categories))
               data/posts)
       (for [{:keys [categories] :as post} data/posts
             category categories]
         [category post])))

;; filter
(filter even? (range 10)) ; (0 2 4 6 8)

(filter #(> % 5) (range 10))

(filter #(= (count %) 3) data/strings-vec) ; only when length of word is 3

(is (= '("bla") (filter #{"bla"} data/strings-vec))) ; only returns the found value

;; filterv - returns a vector rather than a sequence.
(filterv even? (range 10)) ; [0 2 4 6 8]

;; example of a function taking function as argument
(defn total_price
  [price f-fee]
  (+ price (f-fee price)))

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

; function as argument
(defn test-and-inc
  [tst-fn x]
  (if (tst-fn x)
    (inc x)
    x))

(test-and-inc odd? 3) ; 4
(test-and-inc odd? 4) ; 4

; function runner examples

(defn function-runner
  "left-to-right, needs arg
   this really is a reducing pattern 
   where you call each function on the result of prev function call
   essentially same as -> or other threading fn's when not fn's in coll"
  ([arg f] (f arg))
  ([arg f g] (g (f arg)))
  ([arg f g h] (h (g (f arg))))
  ([arg f g h i] (-> (f arg) (g) (h) (i)))
  ([arg f g h i j] (-> (f arg) (g) (h) (i) (j))))

(defn function-runner-reduc
  "issue: on calling self (next fs) = list! which using & fs turns into a list of list
   resolve:
   - add another fn and call that without &
   - on first input, have all fs in a list/vec and remove &
   - don't call self, use loop/recur - see function-runner-loop
   - use reduce - see function-runner-reduce
   - with comp - see function-runner-comp"
  [arg & fs]
  ;; (println arg)
  ;; (println fs)
  (let [flat (flatten fs)
        ; f (first flat)
        ; n (next flat)
        [f & n] flat ; destructure
        ] ; use next, not rest
    ;; (println flat)
    ;; (println f)
    ;; (println n)
    (if f
      (function-runner-reduc (f arg) n)
      arg)))

(defn function-runner-loop
  [arg & fs]
  (loop [rs arg
         fs-coll fs]
    (let [;f (first fs-coll)
          [f & n] fs-coll]
      (if f ; could also do if-let & assign right away if f
        (recur (f rs) n); (next fs-coll))
        rs))))

; with reduce: assign result of (f rs) to rs every time for coll of fs
(defn function-runner-reduce
  [arg & fs]
  (reduce (fn [rs f] (f rs)) arg fs))

(defn function-runner-comp
  "create comp fn with reverse of fs then call on arg"
  [arg & fs]
  ((apply comp (reverse fs)) arg))

; create a fn that will run all function-runner's separately
(def fr (juxt function-runner
              function-runner-reduc
              function-runner-loop
              function-runner-reduce
              function-runner-comp))

(fr 10 inc)
(fr 10 inc dec)
(fr 10 inc dec)
(fr 10 #(* % %))
(fr 10 #(* % %) #(/ % 2))
(fr 10 #(* % %) #(/ % 2) #(+ % 9))
(fr 10 #(* % %) #(/ % 2) #(+ % 9) #(- % 33))

; another example of fn caller
(defn my-apply-two
  [f1 f2 arg]
  (f1 (f2 arg)))

(my-apply-two inc inc 5) ; 7
(my-apply-two inc #(* 5 %) 5) ; 26

; very simple Higher Order example:
(defn two
  "call f with arg 2"
  [f]
  (f 2))

(two #(* % %)) ; square
(two #(* % % %)) ; cube
(two range) ; generate range
(two #(take % (repeat 1))) ; generate seq of 1's
(two #(* 2 %)) ; exponential to the power of 2
(two identity) ; return arg
(two str) ; turn to string
(two #(conj [] %)) ; turn to vec
(two #(nth [9 8 7 6] %)) ; get value at index
