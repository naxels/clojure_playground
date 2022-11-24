(ns mbezjak.core-extensions.pairs-test
  (:require
   [clojure.test :refer [deftest is]]
   [mbezjak.core-extensions.pairs :as sut]))

(deftest map-1
  (is (= [[:a 1] [:b 2]]
         (sut/map-1 keyword [["a" 1] ["b" 2]]))))

(deftest map-2
  (is (= [[1 :a] [2 :b]]
         (sut/map-2 keyword [[1 "a"] [2 "b"]]))))

(deftest filter-1
  (is (= [[:a 1]]
         (sut/filter-1 #(= :a %) [[:a 1] [:b 2]]))))

(deftest filter-2
  (is (= [[:a 1]]
         (sut/filter-2 #(= 1 %) [[:a 1] [:b 2]]))))

(deftest firsts
  (is (= [:a :b] (sut/firsts [[:a 1] [:b 2]]))))

(deftest seconds
  (is (= [1 2] (sut/seconds [[:a 1] [:b 2]]))))