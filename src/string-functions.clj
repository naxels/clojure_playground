(ns string-functions
  (:require [clojure.string]))

; replace
(clojure.string/replace "Hello world" 
                        #"\w" 
                        (fn [letter] (println letter) "!"))

(clojure.string/replace "Hello 12345" #"\d" "a")