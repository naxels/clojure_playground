(ns data)

; lists
(def candy '("chocolate" "jelly" "mint"))
(def kids '("Mike" "Anna" "Ted" "Mary" "Alex" "Emma" "Piet"))

; vectors
(def coll-vec [1 2 3 4 5 6 7 8 9 0 15])

(def user-info ["robert8990" 2011 :name "Bob" :city "Boston"])

(def my-nested-vector [:a :b :c :d [:x :y :z]])

(def strings-vec ["string1" "string2" "bla"])

(def test-coll [0 1 2 3 4])

;; vector containing maps, self made
(def customers [{:name "henk" :purchases [{:total 5}
                                          {:total 6}
                                          {:total 7}]}
                {:name "piet" :purchases [{:total 100} ; only 2 purchases, so filtered out
                                          {:total 200}]}
                {:name "jaap" :purchases [{:total 10}
                                          {:total 20}
                                          {:total 30}]}])

(def articles
  [{:title "Another win for India"
    :date "2017-11-23"
    :ads [2 5 8]
    :author "John McKinley"}
   {:title "Hottest day of the year"
    :date "2018-08-15"
    :ads [1 3 5]
    :author "Emma Cribs"}
   {:title "Expected a rise in Bitcoin shares"
    :date "2018-12-11"
    :ads [2 4 6]
    :author "Zoe Eastwood"}])

(def match_scores [{:winner_name "Roger Federer" :loser_name "Piet"}
                   {:winner_name "Piet" :loser_name "Jaap"}
                   {:winner_name "Piet" :loser_name "Roger Federer"}])

; maps
(def coll-map {:a 1 :b 2 :c 3 :d 4 :e 5})
(def coll-map-nested {:a "1" :b "2" :c {:x1 {:x2 "z1"}}})
(def coll-map-words {"morning" 2 "bye" 1 "hi" 5 "gday" 2})

(def enemy {:name "Zulkaz", :health 250, :armor 0.8, :camp :trolls})

(def person {:name "Romeo"
             :age 16
             :gender :male
             :bike "Giant"
             :house :rental})

(def persons [{:name "Romeo"
               :age 16
               :gender :male
               :bike "Giant"
               :house :rental}
              {:name "Piet"}])

; map with subtree
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

(def unordered '({:created_at 1612637437}
                 {:created_at 1610998684}
                 {:created_at 1622245799}))