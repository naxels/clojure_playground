(ns destructure
  (:require clojure.string
            data))

;; https://clojure.org/guides/destructuring

; functions on user-info
(let [[_username _account-year & extra-info] data/user-info ; turn user-info into a destruct
      {:keys [name city]} (apply hash-map extra-info)] ; turn extra-info into a map
  (format "%s is in %s" name city))

; making it even shorter by destructuring the & right away
(let [[_username _account-year & {:keys [name city]}] data/user-info]
  (format "%s is in %s" name city))

; https://purelyfunctional.tv/issues/purelyfunctional-tv-newsletter-371-chain-map-filter-and-reduce/
; functions on hospital
(defn average-of-attendings []
  ; lot of variables are assigned in let to make the final function easy
  (let [staff (-> data/hospital :staff vals) ; not really a destruct, just calling the keys down the map to get the vals of staff what we need
        attendings (->> staff
                        (filter #(= :attending (:position %))))
        sum (->> attendings
                 (map :salary)
                 (reduce + 0))]
    (/ sum (count attendings))))

; destruct only :staff key of hospital to staff
(let [{:keys [staff]} data/hospital]
      (first staff))

; functions on person
(defn character-desc
        [{:keys [name gender] age-in-years :age}] ; do both destruct by keys and self defined
        (str "Name: " name " age: " age-in-years " gender: " gender))

(defn add-greeting [{:keys [name age] :as character}] ; destruct with keys and :as for the full map
	  (assoc character ; add to the incoming map
          :greeting
          (str "Hello, my name is " name " and I am " age ".")))

;; get a vector with map to destruct
(def vec-of-maps [{:name "Piet"}
                  {:name "Henk"}])

; functions on vec-of-maps
(let [[x y z] vec-of-maps ; destruct a vector, note that z doesn't exist and is empty and that x and y will be the map
      {:keys [name]} x] ; so we can destruct the map further
  (str "X= " x "Y= " y "Z= " z "name= " name))

; https://gist.github.com/john2x/e1dca953548bfdfb9844

(let [[a _ _ d [x y z]] data/my-nested-vector] ; with the underscore you skip assignment
  (println a d x y z))

(let [[a b & the-rest] data/my-nested-vector] ; the & makes a Vector of the rest of the data structure
  (println a b the-rest (class the-rest)))

; advanced
(let [[a _ _ _ [x y z :as nested] :as all] data/my-nested-vector] ; you can have multiple :as for subsections
  (println a x y z nested all))

; vs anonymous function
(fn [x & rest]
  (- x (apply + rest)))

#(- % (apply + %&))

; :or and keyword arguments
(defn make-user
  "return a map"
  [username & {:keys [email join-date]
               :or {join-date (java.util.Date.)}}]
  {:username username
   :join-date join-date
   :email email
   ; 2.592e9 -> one month in ms
   :exp-date (java.util.Date. (long (+ 2.592e9 (.getTime join-date))))})

(make-user "Bobby")
(make-user "Bobby" :join-date (java.util.Date. 111 0 1))
(make-user "Bobby" :join-date (java.util.Date. 111 0 1) :email "bobby@example.com")

; function for mapping over
(defn first-name-upcase
  "Only takes the first-name key from the map"
  [{:keys [first-name]}] ; in Clojure, specific key destruct is preferred, easier to reason about
  (clojure.string/upper-case first-name))

(map first-name-upcase (vals (:staff data/hospital))) ; from hospital, take staff, then only the values (which are maps)

; https://otavio.dev/2021/08/12/clojure-journey-ix-destructuring/

;; just the 1st and 2nd value
(let [[first second] data/coll-vec]
  (println first second))

;; get the deeply nested value in a map using destructuring
(defn address-number [{{number :number} :address}]
  (println number))

(address-number data/deeply-nested)

; using keys
(defn address-number-k [{{:keys [number]} :address}]
  (println number))

(address-number-k data/deeply-nested)

; with :or you can protect against not found, nil, when you want to work with a value <- very handy with JSON
(defn get-doc [{:keys [doc] :or {doc "xxx"}}]
  (println doc))

(get-doc data/deeply-nested) ;; xxx

; example of :or in deeply nested, even works when the first map is not found
(defn get-zip [{{:keys [zip] :or {zip "1234AA"}} :address}]
  (println zip))

(get-zip data/deeply-nested)