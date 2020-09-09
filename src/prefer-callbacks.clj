(ns prefer-callbacks
  (:require [clojure.test :refer [is]]))


; from PurelyFunctional.tv Newsletter 384

; Eric says that instead of naming the steps/function, you should name the 'callback', so:

;; dataset, self made
(def customers [{:name "henk" :purchases [{:total 5}
                                          {:total 6}
                                          {:total 7}]}
                {:name "piet" :purchases [{:total 100} ; only 2 purchases, so filtered out
                                          {:total 200}]}
                {:name "jaap" :purchases [{:total 10}
                                          {:total 20}
                                          {:total 30}]}])


;; original code
(->> customers
     (filter #(>= (count (:purchases %)) 3)) ; only customers with 3+ purchases
     (map #(apply max-key :total (:purchases %))))  ; get biggest purchase


;; naming the steps/function:
(defn select-best-customers [customers]
  (filter #(>= (count (:purchases %)) 3) customers)) ; copy/paste of previous

(defn get-biggest-purchases [customers]
  (map #(apply max-key :total (:purchases %)) customers)) ; copy/paste of previous

(->> customers
     select-best-customers
     get-biggest-purchases)

;; name the 'callback':
(defn good-customer? [customer]
  (>= (count (:purchases customer)) 3))

(defn biggest-purchase [customer]
  (apply max-key :total (:purchases customer)))

(->> customers
     (filter good-customer?)
     (map biggest-purchase))