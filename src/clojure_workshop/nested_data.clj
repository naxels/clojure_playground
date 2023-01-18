(ns clojure-workshop.nested-data)

; Ex 2.06

; data
(def gemstone-db {:ruby {:name "Ruby"
                         :stock 120
                         :sales [1990 3644 6376 4918 7882 6747 7495 8573 5097 1712]
                         :properties {:dispersion 0.018
                                      :hardness 9.0
                                      :refractive-index [1.77 1.78]
                                      :color "Red"}}
                  :diamond {:name "Diamond"
                            :stock 10
                            :sales [8295 329 5960 6118 4189 3436 9833 8870 9700 7182 7061 1579]
                            :properties {:dispersion 0.044
                                         :hardness 10
                                         :refractive-index [2.417 2.419]
                                         :color "Typically yellow, brown or gray to colorless"}}
                  :moissanite {:name "Moissanite"
                               :stock 45
                               :sales [7761 3220]
                               :properties {:dispersion 0.104
                                            :hardness 9.5
                                            :refractive-index [2.65 2.69]
                                            :color "Colorless, green, yellow"}}})

(defn durability
  "get durability of gem in db"
  [db gemstone]
  (get-in db [gemstone :properties :hardness])) ; gemstone is dynamic and part of get-in vector

(defn update-color
  "change the color of gem in db"
  [db gemstone new-color]
  (assoc-in db [gemstone :properties :color] new-color)) ; returns entire coll with change

(defn sell
  "decrease stock by 1 & add client_id to sales
   NOTE: you don't update the data, you get new data structures with the result of operation"
  [db gemstone client_id]
  (-> db ; put db as first arg
      (update-in [gemstone :stock] dec)
      (update-in [gemstone :sales] conj client_id))) ; conj vector with client_id