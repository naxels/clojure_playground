(ns game-values)

(def game-users
  [{:id 9342
    :username "speedy"
    :current-points 45
    :remaining-lives 2
    :experience-level 5
    :status :active}
   {:id 9854
    :username "stealthy"
    :current-points 1201
    :remaining-lives 1
    :experience-level 8
    :status :speed-boost}
   {:id 3014
    :username "sneaky"
    :current-points 725
    :remaining-lives 7
    :experience-level 3
    :status :active}
   {:id 2051
    :username "forgetful"
    :current-points 89
    :remaining-lives 4
    :experience-level 5
    :status :imprisoned}
   {:id 1032
    :username "wandering"
    :current-points 2043
    :remaining-lives 12
    :experience-level 7
    :status :speed-boost}
   {:id 7213
    :username "slowish"
    :current-points 143
    :remaining-lives 0
    :experience-level 1
    :status :speed-boost}
   {:id 5633
    :username "smarter"
    :current-points 99
    :remaining-lives 4
    :experience-level 4
    :status :terminated}
   {:id 3954
    :username "crafty"
    :current-points 21
    :remaining-lives 2
    :experience-level 8
    :status :active}
   {:id 7213
    :username "smarty"
    :current-points 290
    :remaining-lives 5
    :experience-level 12
    :status :terminated}
   {:id 3002
    :username "clever"
    :current-points 681
    :remaining-lives 1
    :experience-level 8
    :status :active}])

;; I was spot on with my functions compared to solution :) :)

(defn max-value-by-status
  [field status users]
  (->> users
       (filter #(= status (:status %)))
       (map field)
       (apply max 0)))

(defn min-value-by-status
  [field status users]
  (->> users
       (filter #(= status (:status %)))
       (map field)
    ;;    (#(do (println %) %))
       (apply min 0)))


; What is the highest value of :current-points for all the :active users
(max-value-by-status :current-points :active game-users)
(max-value-by-status :current-points :speed-boost game-users)

(min-value-by-status :remaining-lives :imprisoned game-users)
(min-value-by-status :experience-level :terminated game-users)