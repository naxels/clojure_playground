(ns mbezjak.core-extensions.map-test
  {:clj-kondo/config '{:lint-as {clojure.test.check.clojure-test/defspec clj-kondo.lint-as/def-catch-all}}}
  (:require
   [clojure.test :refer [deftest is]]
   [clojure.test.check.clojure-test :refer [defspec]]
   [clojure.test.check.generators :as gen]
   [clojure.test.check.properties :as prop]
   [mbezjak.core-extensions.map :as sut]))

(defspec map-key-works-on-any-map-category
  (prop/for-all
   [m (gen/hash-map gen/any gen/any)]
   (= (type m) (type (sut/map-key m identity)))))

(deftest map-key
  (is (= {} (sut/map-key {} inc)))
  (is (= {1 :b} (sut/map-key {1 :a 2 :b} (constantly 1))))

  (is (= {2 :a 3 :b} (sut/map-key {1 :a 2 :b} inc)))
  (is (sorted? (sut/map-key (sorted-map :a 1 :b 2) identity))))

(defspec map-val-works-on-any-map-category
  (prop/for-all
   [m (gen/hash-map gen/any gen/any)]
   (= (type m) (type (sut/map-val m identity)))))

(deftest map-val
  (is (= {} (sut/map-val {} inc)))
  (is (= {1 :b} (sut/map-key {1 :a 2 :b} (constantly 1))))

  (is (= {:a 2 :b 3} (sut/map-val {:a 1 :b 2} inc)))
  (is (sorted? (sut/map-val (sorted-map :a 1 :b 2) identity))))

(defspec filter-key-works-on-any-map-category
  (prop/for-all
   [m (gen/hash-map gen/any gen/any)]
   (= (type m) (type (sut/filter-key m some?)))))

(deftest filter-key
  (is (= {} (sut/filter-key {} pos-int?)))
  (is (= {1 :a} (sut/filter-key {1 :a -1 :b} pos-int?)))
  (is (sorted? (sut/filter-key (sorted-map :a 1 :b 2) some?))))

(defspec filter-val-works-on-any-map-category
  (prop/for-all
   [m (gen/hash-map gen/any gen/any)]
   (= (type m) (type (sut/filter-val m some?)))))

(deftest filter-val
  (is (= {} (sut/filter-val {} pos-int?)))
  (is (= {:a 1} (sut/filter-val {:a 1 :b -1} pos-int?)))
  (is (sorted? (sut/filter-val (sorted-map :a 1 :b 2) some?))))

(defspec remove-key-works-on-any-map-category
  (prop/for-all
   [m (gen/hash-map gen/any gen/any)]
   (= (type m) (type (sut/remove-key m some?)))))

(deftest remove-key
  (is (= {} (sut/remove-key {} pos-int?)))
  (is (= {-1 :b} (sut/remove-key {1 :a -1 :b} pos-int?)))
  (is (sorted? (sut/remove-key (sorted-map :a 1 :b 2) some?))))

(defspec remove-val-works-on-any-map-category
  (prop/for-all
   [m (gen/hash-map gen/any gen/any)]
   (= (type m) (type (sut/remove-val m some?)))))

(deftest remove-val
  (is (= {} (sut/remove-val {} pos-int?)))
  (is (= {:b -1} (sut/remove-val {:a 1 :b -1} pos-int?)))
  (is (sorted? (sut/remove-val (sorted-map :a 1 :b 2) some?))))

(deftest remove-keys
  (is (= {} (sut/remove-keys {} #{})))
  (is (= {} (sut/remove-keys {:a 1} #{:a})))

  (is (= {:a 1} (sut/remove-keys {:a 1} #{})))
  (is (= {:a 1} (sut/remove-keys {:a 1} #{:b}))))

(deftest dissoc-in
  (is (= {:a 1} (sut/dissoc-in {:a 1} [])))
  (is (= {:a 1} (sut/dissoc-in {:a 1} [:z])))
  (is (= {:a {:b 2}} (sut/dissoc-in {:a {:b 2}} [:a :z])))
  (is (= {:a {:b 2}} (sut/dissoc-in {:a {:b 2}} [:y :z])))
  (is (= {:a {:b {:c 3}}} (sut/dissoc-in {:a {:b {:c 3}}} [:a :y :z])))

  (is (= {} (sut/dissoc-in {:a 1} [:a])))
  (is (= {:b 2} (sut/dissoc-in {:a 1 :b 2} [:a])))
  (is (= {:a {}} (sut/dissoc-in {:a {:b 2}} [:a :b])))
  (is (= {:a {}} (sut/dissoc-in {:a {:b {:c 3}}} [:a :b])))
  (is (= {:a {:b {}}} (sut/dissoc-in {:a {:b {:c 3}}} [:a :b :c]))))

(deftest update-when
  (is (= {} (sut/update-when {} :a string? #(str % "abc"))))
  (is (= {:a nil} (sut/update-when {:a nil} :a string? #(str % "abc"))))
  (is (= {:a 1} (sut/update-when {:a 1} :a string? #(str % "abc"))))
  (is (= {:a "Xabc"} (sut/update-when {:a "X"} :a string? #(str % "abc")))))

(deftest update-if
  (is (= {} (sut/update-if {} :a inc)))
  (is (= {:a 2} (sut/update-if {:a 1} :a inc)))
  (is (= {:a "abc"} (sut/update-if {:a nil} :a #(str % "abc")))))

(deftest assoc-if
  (is (= {} (sut/assoc-if {} :a 1)))
  (is (= {:a 2} (sut/assoc-if {:a 1} :a 2)))
  (is (= {:a 1} (sut/assoc-if {:a nil} :a 1))))

(deftest submap?
  (is (true? (sut/submap? {} {})))
  (is (true? (sut/submap? {:a 1} {})))
  (is (true? (sut/submap? {:a 1 :b 2} {:a 1})))
  (is (true? (sut/submap? {:a 1 :b 2} {:a 1 :b 2})))
  (is (false? (sut/submap? {} {:a 1})))
  (is (false? (sut/submap? {:a 1} {:a 2})))
  (is (false? (sut/submap? {:a 1} {:b 1}))))

(deftest unqualified
  (is (= {} (sut/unqualified {})))
  (is (= {:a 1} (sut/unqualified {:a 1})))
  (is (= {:a 1} (sut/unqualified {:user/a 1}))))