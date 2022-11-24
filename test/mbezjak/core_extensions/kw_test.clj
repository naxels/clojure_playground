(ns mbezjak.core-extensions.kw-test
  (:require
   [clojure.test :refer [deftest is]]
   [mbezjak.core-extensions.kw :as sut]))

(deftest str-test
  (is (= "a" (sut/str :a)))
  (is (= "test/a" (sut/str :test/a))))