(ns mbezjak.core-extensions.coll-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [clojure.test.check.clojure-test :refer [defspec]]
   [clojure.test.check.generators :as gen]
   [clojure.test.check.properties :as prop]
   [mbezjak.core-extensions.coll :as sut]))

(deftest coll-any?
  (is (false? (sut/any? identity nil)))
  (is (false? (sut/any? identity [])))
  (is (false? (sut/any? identity [nil false])))
  (is (false? (sut/any? even? [1 3 5])))
  (is (false? (sut/any? false? [true true])))
  (is (true? (sut/any? odd? [1])))
  (is (true? (sut/any? nil? [1 nil 3])))
  (is (true? (sut/any? false? [true true false])))
  (is (true? (sut/any? identity [1 2 3]))))

(defspec includes?-always-returns-a-boolean
  (prop/for-all
   [x    gen/any
    coll (gen/one-of [(gen/vector gen/any)
                      (gen/list gen/any)])]
   (boolean? (sut/includes? x coll))))

(defspec includes?-must-return-true-if-collection-includes-element
  (prop/for-all
   [x    gen/any
    coll (gen/one-of [(gen/vector gen/any)
                      (gen/list gen/any)])]
   (true? (sut/includes? x (conj coll x)))))

(deftest includes?
  (is (false? (sut/includes? 1 nil)))
  (is (false? (sut/includes? 1 [])))
  (is (false? (sut/includes? 1 [2])))
  (is (false? (sut/includes? 1 [:a :b])))
  (is (false? (sut/includes? nil [:a :b])))

  (is (true? (sut/includes? 1 [1 2 3])))
  (is (true? (sut/includes? 1 [3 2 1])))
  (is (true? (sut/includes? :a [:b :a :c])))
  (is (true? (sut/includes? nil [1 nil 3]))))

(deftest elem
  (is (nil? (sut/elem some? nil)))
  (is (nil? (sut/elem some? [])))
  (is (nil? (sut/elem some? [nil])))
  (is (nil? (sut/elem true? [false])))

  (is (true? (sut/elem true? [false true])))
  (is (= 1 (sut/elem #(= 1 %) [1 2 3])))
  (is (= 1 (sut/elem #(= 1 %) [3 2 1])))
  (is (= :a (sut/elem #(= :a %) [:b :a :c])))
  (is (= :a (sut/elem #(= :a %) [:b :a :c :a :e :a]))))

(defspec groups-size-returned<=coll-size
  (prop/for-all
   [coll (gen/one-of [(gen/vector gen/any)
                      (gen/list gen/any)])]
   (<= (count (sut/groups some? coll))
       (count coll))))

(defspec groups-stable-order-inside-each-group
  (prop/for-all
   [coll (gen/fmap sort (gen/list gen/large-integer))]
   (every? #(= % (sort %))
           (sut/groups pos-int? coll))))

(deftest groups
  (is (nil? (sut/groups some? nil)))
  (is (nil? (sut/groups some? [])))

  (is (= '([1]) (sut/groups some? [1])))
  (is (= '([1 2]) (sut/groups some? [1 2])))
  (is (= '([1 2] [nil]) (sut/groups some? [1 nil 2])))
  (is (= '([nil] [1 2]) (sut/groups some? [nil 1 2])))

  (is (= '([{:id 1 :name :a} {:id 1 :name :c}]
           [{:id 2 :name :b}])
         (sut/groups :id [{:id 1 :name :a}
                          {:id 2 :name :b}
                          {:id 1 :name :c}]))))

(defspec separate-2-always-returns-a-pair
  (prop/for-all
   [coll (gen/one-of [(gen/vector gen/any)
                      (gen/list gen/any)
                      (gen/set gen/any)])]
   (= 2 (count (sut/separate-2 some? coll)))))

(defspec separate-2-stable-order-inside-each-group
  (prop/for-all
   [coll (gen/fmap sort (gen/list gen/large-integer))]
   (let [[a b] (sut/separate-2 pos-int? coll)]
     (and (= a (seq (sort a)))
          (= b (seq (sort b)))))))

(defspec separate-2-works-even-if-pred-doesnt-return-a-boolean
  (prop/for-all
   [coll (gen/one-of [(gen/vector gen/any)
                      (gen/list gen/any)
                      (gen/set gen/any)])]
   (let [[a b] (sut/separate-2 some? coll)]
     (and (every? some? a)
          (every? nil? b)))))

(deftest separate-2
  (is (= [nil nil] (sut/separate-2 some? nil)))
  (is (= [nil nil] (sut/separate-2 some? [])))

  (is (= ['(1) nil] (sut/separate-2 some? [1])))
  (is (= [nil '(nil)] (sut/separate-2 some? [nil])))
  (is (= ['(:a) '(1)] (sut/separate-2 keyword? [:a 1])))
  (is (= ['(:a :b :c) '(1 2 3)] (sut/separate-2 keyword? [:a 1 :b 2 :c 3]))))

(deftest adjacent-pairs
  (is (= [] (sut/adjacent-pairs nil)))
  (is (= [] (sut/adjacent-pairs [])))
  (is (= [] (sut/adjacent-pairs [1])))
  (is (= [[1 2]] (sut/adjacent-pairs [1 2])))
  (is (= [[1 2] [2 3]] (sut/adjacent-pairs [1 2 3])))
  (is (= [[1 2] [2 3] [3 4]] (sut/adjacent-pairs [1 2 3 4]))))

(deftest combinations-2
  (is (nil? (sut/combinations-2 nil)))
  (is (nil? (sut/combinations-2 [])))
  (is (nil? (sut/combinations-2 [1])))
  (is (= '([1 2]) (sut/combinations-2 [1 2])))
  (is (= '([1 2] [1 3] [2 3])
         (sut/combinations-2 [1 2 3])))
  (is (= '([1 2] [1 3] [1 4] [2 3] [2 4] [3 4])
         (sut/combinations-2 [1 2 3 4])))
  (testing "is lazy"
    (is (= '([0 1] [0 2] [0 3] [0 4] [0 5])
           (take 5 (sut/combinations-2 (range))))))
  (testing "does not consume the stack"
    (is (= 100000
           (count (take 100000 (sut/combinations-2 (range 1000))))))))

(deftest splits-by
  (is (= [] (sut/splits-by keyword? [])))
  (is (= [[1 2]] (sut/splits-by keyword? [1 2])))
  (is (= [[1 2] [3 4]] (sut/splits-by keyword? [1 2 :here 3 4])))
  (is (= [[1] [2] [3] [4]] (sut/splits-by keyword? [1 :here 2 :here 3 :here 4])))
  (is (= [[1]] (sut/splits-by keyword? [1 :here])))
  (is (= [[1] []] (sut/splits-by keyword? [1 :here :here])))
  (is (= [[] [1]] (sut/splits-by keyword? [:here 1])))
  (is (= [[] [] [1]] (sut/splits-by keyword? [:here :here 1])))
  (is (= [[1] [] [2]] (sut/splits-by keyword? [1 :here :here 2]))))