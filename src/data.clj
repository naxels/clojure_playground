(ns data)

; lists
(def candy '("chocolate" "jelly" "mint"))
(def kids '("Mike" "Anna" "Ted" "Mary" "Alex" "Emma" "Piet"))
(def firstnames kids)
(def lastnames '("van Dijk" "Verbeek" "Pannenkoek" "Vollehoven" "Rossum"))

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
; vector of maps
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

; vector of maps
(def persons [{:name "Romeo"
               :age 16
               :gender :male
               :bike "Giant"
               :house :rental}
              {:name "Piet"}])

; map of maps
(def people
  {"Carl"   {:mom "Sofia"
             :dad "Thomas"}
   "Sofia"  {:mom "Elena"
             :dad :unknown}
   "Dexter" {:mom :unknown
             :dad :unknown}
   "Thomas" {:mom :unknown
             :dad "Dexter"}
   "Elena"  {:mom :unknown
             :dad :unknown}})

(def map-of-map-vec {1 {:good [1 2] :bad [3 4]}
                     2 {:good [5 6] :bad [7 8]}})

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

; list of maps
(def unordered '({:created_at 1612637437}
                 {:created_at 1610998684}
                 {:created_at 1622245799}))

(def list-one '(1 2 3 4 5 6 7 8 9 0))
(def list-two '(1 2 3 4 5 5 6 6 9 0))
(def list-vec-combined '([:a :b] [:b :b] [:a :c] [:c :c] [:d :d]))

; list of maps with a sub set
(def posts '({:title "Blambda!"
              :file "2022-07-03-blambda.md"
              :categories #{"aws" "s3" "lambda" "clojure"}
              :date "2022-07-03"}
             {:title "Dogfooding Blambda! : revenge of the pod people"
              :file "2022-07-04-dogfooding-blambda-1.md"
              :categories #{"aws" "s3" "lambda" "clojure" "blambda"}
              :date "2022-07-04"}
             {:title "Hacking the blog: favicon"
              :file "2022-07-05-hacking-blog-favicon.md"
              :categories #{"clojure" "blog"}
              :date "2022-07-05"}
             {:title "Hacking the blog: categories"
              :file "2022-07-06-hacking-blog-categories.md"
              :categories #{"clojure" "blog"}
              :date "2022-07-06"}))

(def deeply-nested {:name "Otavio"
                    :last-name "Valadares"
                    :contact {:phone "+550000000000"
                              :email "xpto@xpto.com"}
                    :address {:country :brazil
                              :state :sp
                              :street "Xpto Street"
                              :number "42"}})

(def xml-example "<balance>
                    <accountId>3764882</accountId>
                    <currentBalance>80.12389</currentBalance>
                    <contract>
                      <contractId>77488</contractId>
                      <currentBalance>1921.89</currentBalance>
                    </contract>
                  </balance>")
