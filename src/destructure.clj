(ns destructure)

;; https://clojure.org/guides/destructuring

; vector
(def user-info ["robert8990" 2011 :name "Bob" :city "Boston"])
; functions on user-info
(let [[_username _account-year & extra-info] user-info ; turn user-info into a destruct
      {:keys [name city]} (apply hash-map extra-info)] ; turn extra-info into a map
  (format "%s is in %s" name city))

; map with subtree
; https://purelyfunctional.tv/issues/purelyfunctional-tv-newsletter-371-chain-map-filter-and-reduce/
(def hospital
  {:hospital "Hans Jopkins"
   :staff {1 {:first-name "John"
              :last-name  "Doe"
              :salary 40000
              :position :resident}
           2 {:first-name "Jane"
              :last-name "Deer"
              :salary 100000
              :position :attending}
           3 {:first-name "Sam"
              :last-name "Waterman"
              :salary 0
              :position :volunteer}}})

; functions on hospital
(defn average-of-attendings []
  ; lot of variables are assigned in let to make the final function easy
  (let [staff (-> hospital :staff vals) ; not really a destruct, just calling the keys down the map to get the vals of staff what we need
        attendings (->> staff
                        (filter #(= :attending (:position %))))
        sum (->> attendings
                 (map :salary)
                 (reduce + 0))]
    (/ sum (count attendings))))

; destruct only :staff key of hospital to staff
(let [{:keys [staff]} hospital]
      (first staff))

; simple map
(def person {:name "Romeo" :age 16 :gender :male :bike "Giant" :house :rental})

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

(def my-nested-vector [:a :b :c :d [:x :y :z]])

(let [[a _ _ d [x y z]] my-nested-vector] ; with the underscore you skip assignment
  (println a d x y z))

(let [[a b & the-rest] my-nested-vector] ; the & makes a Vector of the rest of the data structure
  (println a b the-rest (class the-rest)))

; advanced
(let [[a _ _ _ [x y z :as nested] :as all] my-nested-vector] ; you can have multiple :as for subsections
  (println a x y z nested all))