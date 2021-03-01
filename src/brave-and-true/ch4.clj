(ns ch4 
  (:require [clojure.string :as string]))

;; Programming to Abstractions
;; ---------------------------

;; not maplist or maphash, just map

;; map works on linked lists.
(map #(+ % 2) '(1 2 3))

;; [] '() #{} ;; implement the seq abstraction
;; ;; [].cons [].first [].rest '().cons
;; map reduce sort take ;; _use_

;; (range)
;; (range) ;; LazySeq

;; (defn blah []
;;   (take 2 (range)) ;;=> LazySeq)

;; (map #(= 1 %) [1 nil 2 3])
;; (map #(= 1 %) nil)
(cons nil [])

;; map works on hash-maps.
(def h {:a 1 :b 2 :c 3})
(def map-of-hash (map identity h))
map-of-hash
(type (first map-of-hash))

;; identity fn
(identity 1)
(identity :a)
(identity {:a 1 :b 2})
(identity '(1 2 3))
(identity #{1 [1 2]})

;; reduce works on maps (bring up reduce-kv)
(reduce #(into %1 %2) [] h)


(defn titleize
  [topic]
  (str topic " for the Brave and True"))

(map titleize ["Hamsters" "Ragnarok"])
; => ("Hamsters for the Brave and True" "Ragnarok for the Brave and True")

(map titleize '("Empathy" "Decorating"))
; => ("Empathy for the Brave and True" "Decorating for the Brave and True")

(map titleize #{"Elbows" "Soap Carving"})
; => ("Elbows for the Brave and True" "Soap Carving for the Brave and True")

(map #(titleize (second %)) {:uncomfortable-thing "Winking"})
; => ("Winking for the Brave and True")



;; Indirection / Polymorphism

(seq '(1 2 3))
; => (1 2 3)

(seq [1 2 3])
; => (1 2 3)

(seq #{1 2 3})
; => (1 2 3)

(seq {:name "Bill Compton" :occupation "Dead mopey guy"})
; => ([:name "Bill Compton"] [:occupation "Dead mopey guy"])

(into {} (seq {:a 1 :b 2 :c 3}))
; => {:a 1, :c 3, :b 2}



;; Seq Function Examples (Yeeeess!!)

(map inc [1 2 3])
; => (2 3 4)

(map str ["a" "b" "c"] ["A" "B" "C"])
; => ("aA" "bB" "cC")

(list (str "a" "A") (str "b" "B") (str "c" "C"))

;; Vampire diet

(def human-consumption   [8.1 7.3 6.6 5.0])
(def critter-consumption [0.0 0.2 0.3 1.1])
(defn unify-diet-data
  [human critter]
  {:human human
   :critter critter})

(map unify-diet-data human-consumption critter-consumption)
;;  => ({:human 8.1, :critter 0.0}
;;       {:human 7.3, :critter 0.2}
;;       {:human 6.6, :critter 0.3}
;;       {:human 5.0, :critter 1.1})

(def sum #(reduce + %))
(def avg #(/ (sum %) (count %)))
(defn stats
  [numbers]
  (map #(% numbers) [sum count avg]))

(stats [3 4 10])
; => (17 3 17/3)

(stats [80 1 44 13 6])
; => (144 5 144/5)

(def identities
  [{:alias "Batman" :real "Bruce Wayne"}
   {:alias "Spider-Man" :real "Peter Parker"}
   {:alias "Santa" :real "Your mom"}
   {:alias "Easter Bunny" :real "Your dad"}])

(map :real identities)
; => ("Bruce Wayne" "Peter Parker" "Your mom" "Your dad")


;; reduce :) 

(reduce (fn [new-map [key val]]
          (assoc new-map key (inc val)))
        {}
        {:max 30 :min 10})
; => {:max 31, :min 11}

(assoc (assoc {} :max (inc 30))
       :min (inc 10))

;; reduce vamp diet log, nom.

(reduce (fn [new-map [key val]]
          (if (> val 4)
            (assoc new-map key val)
            new-map))
        {}
        {:human 4.1
         :critter 3.9})
; => {:human 4.1}



;; give and take :) 

;; i.e. take, drop, take-while, and drop-while

(take 3 [1 2 3 4 5 6 7 8 9 10])
; => (1 2 3)

(drop 3 [1 2 3 4 5 6 7 8 9 10])
; => (4 5 6 7 8 9 10)

;; Vampire food journal! 

(def food-journal
  [{:month 1 :day 1 :human 5.3 :critter 2.3}
   {:month 1 :day 2 :human 5.1 :critter 2.0}
   {:month 2 :day 1 :human 4.9 :critter 2.1}
   {:month 2 :day 2 :human 5.0 :critter 2.5}
   {:month 3 :day 1 :human 4.2 :critter 3.3}
   {:month 3 :day 2 :human 4.0 :critter 3.8}
   {:month 4 :day 1 :human 3.7 :critter 3.9}
   {:month 4 :day 2 :human 3.7 :critter 3.6}])

(take-while #(< (:month %) 3) food-journal)
;;  => ({:month 1 :day 1 :human 5.3 :critter 2.3}
;;       {:month 1 :day 2 :human 5.1 :critter 2.0}
;;       {:month 2 :day 1 :human 4.9 :critter 2.1}
;;       {:month 2 :day 2 :human 5.0 :critter 2.5})

(drop-while #(< (:month %) 3) food-journal)
;;  => ({:month 3 :day 1 :human 4.2 :critter 3.3}
;;       {:month 3 :day 2 :human 4.0 :critter 3.8}
;;       {:month 4 :day 1 :human 3.7 :critter 3.9}
;;       {:month 4 :day 2 :human 3.7 :critter 3.6})

(take-while #(< (:month %) 4)
            (drop-while #(< (:month %) 2) food-journal))
;;  => ({:month 2 :day 1 :human 4.9 :critter 2.1}
;;       {:month 2 :day 2 :human 5.0 :critter 2.5}
;;       {:month 3 :day 1 :human 4.2 :critter 3.3}
;;       {:month 3 :day 2 :human 4.0 :critter 3.8})


;; filter and some :) 

(filter #(< (:human %) 5) food-journal)
;;  => ({:month 2 :day 1 :human 4.9 :critter 2.1}
;;       {:month 3 :day 1 :human 4.2 :critter 3.3}
;;       {:month 3 :day 2 :human 4.0 :critter 3.8}
;;       {:month 4 :day 1 :human 3.7 :critter 3.9}
;;       {:month 4 :day 2 :human 3.7 :critter 3.6})

(filter #(< (:month %) 3) food-journal)
;;  => ({:month 1 :day 1 :human 5.3 :critter 2.3}
;;       {:month 1 :day 2 :human 5.1 :critter 2.0}
;;       {:month 2 :day 1 :human 4.9 :critter 2.1}
;;       {:month 2 :day 2 :human 5.0 :critter 2.5})

;; Vamp food journal ex :o

(some #(> (:critter %) 5) food-journal)
; => nil

(some #(> (:critter %) 3) food-journal)
; => true

(some #(and (> (:critter %) 3) %) food-journal)
; => {:month 3 :day 1 :human 4.2 :critter 3.3}


;; sort and sort-by

(sort [3 1 2])
; => (1 2 3)

(sort-by count ["aaa" "c" "bb"])
; => ("c" "bb" "aaa")

;; concat

(concat [1 2] [3 4])
; => (1 2 3 4)


;; Lazy Seq !! :) 


(def vampire-database
  {0 {:makes-blood-puns? false, :has-pulse? true  :name "McFishwich"}
   1 {:makes-blood-puns? false, :has-pulse? true  :name "McMackson"}
   2 {:makes-blood-puns? true,  :has-pulse? false :name "Damon Salvatore"}
   3 {:makes-blood-puns? true,  :has-pulse? true  :name "Mickey Mouse"}})

(defn vampire-related-details
  [social-security-number]
  (Thread/sleep 1000)
  (get vampire-database social-security-number))

(defn vampire?
  [record]
  (and (:makes-blood-puns? record)
       (not (:has-pulse? record))
       record))

(defn identify-vampire
  [social-security-numbers]
  (first (filter vampire?
                 (map vampire-related-details social-security-numbers))))

;; (time (vampire-related-details 0))
; => "Elapsed time: 1001.042 msecs"
; => {:name "McFishwich", :makes-blood-puns? false, :has-pulse? true}


;; (time (def mapped-details (map vampire-related-details (range 0 1000000))))
; => "Elapsed time: 0.049 msecs"
; => #'user/mapped-details


;; (time (first mapped-details))
; => "Elapsed time: 32030.767 msecs"
; => {:name "McFishwich", :makes-blood-puns? false, :has-pulse? true}


;; (time (first mapped-details))
; => "Elapsed time: 0.022 msecs"
; => {:name "McFishwich", :makes-blood-puns? false, :has-pulse? true}

;; (time (identify-vampire (range 0 1000000)))
; "Elapsed time: 32019.912 msecs"
; => {:name "Damon Salvatore", :makes-blood-puns? true, :has-pulse? false}

;; Because lazy makes it possible... Infinite Sequences!

(concat (take 8 (repeat "na")) ["Batman!"])
; => ("na" "na" "na" "na" "na" "na" "na" "na" "Batman!")

(take 3 (repeatedly (fn [] (rand-int 10))))
; => (1 4 0)

;; Our own version of an infinite sequence:

(defn even-numbers
  ([] (even-numbers 0))
  ([n] (cons n (lazy-seq (even-numbers (+ n 2))))))

(take 10 (even-numbers))
; => (0 2 4 6 8 10 12 14 16 18)

(cons 0 '(2 4 6))
; => (0 2 4 6)

;; It's mentally relieving compared to conj and into.
;; List? Vec? It's always at the beginning. 
(cons 0 [2 4 6])




;; The Collection Abstraction

(count [:a :b])
; => 2

(count {:a 2 :b 'Heyo :c [1 2]})
; => 3

(empty? [])
; => true

(empty? ["no!"])
; => false

(every? #(= % "cashews") ["cashews" "cashews"])
; => true

(every? #(= % "cashews") ["cashews" "peanuts"])
; => false


;; into !

(map identity {:sunlight-reaction "Glitter!"})
; => ([:sunlight-reaction "Glitter!"])

(into {} (map identity {:sunlight-reaction "Glitter!"}))
; => {:sunlight-reaction "Glitter!"}

;; Other structures

(map identity [:garlic :sesame-oil :fried-eggs])
; => (:garlic :sesame-oil :fried-eggs)

(into [] (map identity [:garlic :sesame-oil :fried-eggs]))
; => [:garlic :sesame-oil :fried-eggs]

;; Vector to list to set

(map identity [:garlic-clove :garlic-clove])
; => (:garlic-clove :garlic-clove)

(into #{} (map identity [:garlic-clove :garlic-clove]))
; => #{:garlic-clove}

;; Non-empty starting collection

(into {:favorite-emotion "gloomy"} [[:sunlight-reaction "Glitter!"]])
; => {:favorite-emotion "gloomy" :sunlight-reaction "Glitter!"}

(into ["cherry"] '("pine" "spruce"))
; => ["cherry" "pine" "spruce"]

(into {:favorite-animal "kitty"} {:least-favorite-smell "dog"
                                  :relationship-with-teenager "creepy"})
;;  => {:favorite-animal "kitty"
;;       :relationship-with-teenager "creepy"
;;       :least-favorite-smell "dog"}

;; The almight conj !

(conj [0] [1])
; => [0 [1]]

(into [0] [1])
; => [0 1]

(conj [0] 1)
; => [0 1]


;; Conj as many as you want! 

(conj [0] 1 2 3 4)
; => [0 1 2 3 4]

(conj {:time "midnight"} [:place "ye olde cemetarium"])
; => {:place "ye olde cemetarium" :time "midnight"}

;; conj in terms of into

(defn my-conj
  [target & additions]
  (into target additions))

(my-conj [0] 1 2 3)
; => [0 1 2 3]


;; Function functions... or higher order functions! :) 

;; apply , the explodey function

(max 0 1 2)
; => 2

;; (max [0 1 2])
; => [0 1 2]

(apply max [0 1 2])
; => 2

;; into in terms of conj

(defn my-into
  [target additions]
  (apply conj target additions))

(my-into [0] [1 2 3])
; => [0 1 2 3]

;; I'm... partial... to the partial function (not really)

(def add10 (partial + 10))
(add10 3)
; => 13
(add10 5)
; => 15

(def add-missing-elements
  (partial conj ["water" "earth" "air"]))

(add-missing-elements "unobtainium" "adamantium")
; => ["water" "earth" "air" "unobtainium" "adamantium"]

;; Handmade partial

(defn my-partial
  [partialized-fn & args]
  (fn [& more-args]
    (apply partialized-fn (into args more-args))))

(def add20 (my-partial + 20))
(add20 3)
; => 23

(fn [& more-args]
  (apply + (into [20] more-args)))

(defn lousy-logger
  [log-level message]
  (condp = log-level
    :warn (string/lower-case message)
    :emergency (string/upper-case message)))

(def warn (partial lousy-logger :warn))

(warn "Red light ahead")
; => "red light ahead"


;; complement , no puns here because I don't 
;; want people to confuse this word with "complement" >:|

;; (defn identify-humans
;;   [social-security-numbers]
;;   (filter #(not (vampire? %))
;;           (map vampire-related-details social-security-numbers)))

(def not-vampire? (complement vampire?))

(defn identify-humans
  [social-security-numbers]
  (filter not-vampire?
          (map vampire-related-details social-security-numbers)))

(defn my-complement
  [fun]
  (fn [& args]
    (not (apply fun args))))

(def my-pos? (complement neg?))
(my-pos? 1)
; => true

(my-pos? -1)
; => false