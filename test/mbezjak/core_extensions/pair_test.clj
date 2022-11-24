(ns mbezjak.core-extensions.pair-test
  (:require
   [clojure.test :refer [deftest is]]
   [mbezjak.core-extensions.pair :as sut]))

(deftest map-1
  (is (= [2 2] (sut/map-1 [1 2] inc))))

(deftest map-2
  (is (= [1 3] (sut/map-2 [1 2] inc))))