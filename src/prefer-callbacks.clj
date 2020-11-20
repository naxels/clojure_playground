(ns prefer-callbacks
  (:require data))


; from PurelyFunctional.tv Newsletter 384

; Eric says that instead of naming the steps/function, you should name the 'callback', so:

;; original code
(->> data/customers
     (filter #(>= (count (:purchases %)) 3)) ; only customers with 3+ purchases
     (map #(apply max-key :total (:purchases %))))  ; get biggest purchase


;; naming the steps/function:
(defn select-best-customers [customers]
  (filter #(>= (count (:purchases %)) 3) customers)) ; copy/paste of previous

(defn get-biggest-purchases [customers]
  (map #(apply max-key :total (:purchases %)) customers)) ; copy/paste of previous

(->> data/customers
     select-best-customers
     get-biggest-purchases)

;; name the 'callback':
(defn good-customer? [customer]
  (>= (count (:purchases customer)) 3))

(defn biggest-purchase [customer]
  (apply max-key :total (:purchases customer)))

(->> data/customers
     (filter good-customer?)
     (map biggest-purchase))