(ns functional-programming-patterns.functional-patterns.function-builder)

(defn discount
  "only when percentage between 0 and 100
   returns fn"
  [percentage]
  {:pre [(and (>= percentage 0)
              (<= percentage 100))]}
  (fn
    [price]
    (- price (* price percentage 0.01))))

(defn selector
  "only when path
   returns fn"
  [& path]
;;   {:pre [(not (empty? path))]}
  {:pre [(seq path)]} ; idiomatic
  (fn
    [ds]
    (get-in ds path)))

; ----
(def twenty-five-percent-off (discount 25))

; returns error
;; (discount -2)
;; (discount 101)

(apply + (map twenty-five-percent-off [100.0 25.0 50.0 25.0])) ;150.0​ 
(apply + (map twenty-five-percent-off [75.0, 25.0])) ; 75.0”




(def person {:name "Michael Bevilacqua-Linn"})

(def personName (selector :name))
(personName person)
(:name person)

; returns error due to no path
;; (selector)

(def person_address {:address {:street {:name "Fake St."}}})

(def streetName (selector :address :street :name))

(streetName person) ; nil
(streetName person_address)
; same as
(get-in person_address [:address :street :name])


; ----

(defn append-a [s] (str s "a"))
(defn append-b [s] (str s "b"))
(defn append-c [s] (str s "c"))

(def append-cba (comp append-a append-b append-c))

(def request
  {:headers {"Authorization" "auth"
             "X-RequestFingerprint" "fingerprint"}
   :body "body"})

(def request-no-auth
  {:headers {"X-RequestFingerprint" "fingerprint-no-auth"}
   :body "body"})

(defn check-authorization
  "check if Authorization header present
   returns with :principal AUser if so else :principal nil"
  [request]
  (let [auth-header (get-in request [:headers "Authorization"])]
    (assoc request :principal (when auth-header
                                "AUser"))))

(defn log-fingerprint
  "print X-RequestFingerprint and return request"
  [request]
  (let [fingerprint (get-in request [:headers "X-RequestFingerprint"])]
    (println (str "FINGERPRINT=" fingerprint))
    request))

(defn compose-filters
  "return a fn with comp of all filters given"
  [filters]
  (reduce
   (fn
     [all-filters, current-filter]
     (comp all-filters current-filter))
   filters))

(defn compose-filters-apply
  "same as the above compose-filters but with apply"
  [filters]
  (apply comp filters))

; ----

(append-cba "z"); zcba

(def filter-chain (compose-filters [check-authorization log-fingerprint]))

(def filter-chain-apply (compose-filters-apply [check-authorization log-fingerprint]))

(filter-chain request)
(filter-chain-apply request)

(filter-chain request-no-auth)
(filter-chain-apply request-no-auth)