(ns set-operations
  (:require [clojure.set :as set]
            data))

;; set , is filter returning a set
(set/select odd? #{1 2 3 4 5})

;; union
(set/union #{1 2} #{3 4})

;; intersection
(set/intersection #{1 2} #{2 3})

;; difference
(set/difference #{1 2} #{2 3})