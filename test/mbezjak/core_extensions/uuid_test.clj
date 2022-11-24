(ns mbezjak.core-extensions.uuid-test
  (:require
   [clojure.test :refer [deftest is]]
   [mbezjak.core-extensions.uuid :as sut])
  (:import
   (clojure.lang ExceptionInfo)))

(deftest from-string
  (is (uuid? (sut/from-string "f2fbebd7-2620-47ce-9262-506e4aaad754"))))

(deftest coerce
  (is (uuid? (sut/coerce "f2fbebd7-2620-47ce-9262-506e4aaad754")))
  (is (uuid? (sut/coerce #uuid "f2fbebd7-2620-47ce-9262-506e4aaad754")))
  (is (thrown? IllegalArgumentException (sut/coerce "123")))
  (is (thrown? ExceptionInfo (sut/coerce 123))))