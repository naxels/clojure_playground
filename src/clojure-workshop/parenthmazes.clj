(ns clojure-workshop.parenthmazes)

(def weapon-damage {:fists 10.0 :staff 35.0 :sword 100.0 :cast-iron-saucepan 150.0})

;; (defn strike
;;   ([target weapon]
;;    (let [points (weapon weapon-damage)]
;;      (if (= :gnomes (:camp target))
;;        (update target :health + points)
;;        (let [armor (or (:armor target) 0) ; better than using an if
;;              damage (* points (- 1 armor))]
;;          (update target :health - damage))))))

;; (defn strike
;;   "updated with destruct using keys in args"
;;   ([{:keys [camp armor] :as target} weapon]
;;    (let [points (weapon weapon-damage)]
;;      (if (= :gnomes camp)
;;        (update target :health + points)
;;        (let [damage (* points (- 1 (or armor 0)))]
;;          (update target :health - damage))))))

(defn strike
  "With one argument, strike a target with a default :fists `weapon`. 
   With two argument, strike a target with `weapon`.
   Strike will heal a target that belongs to the gnomes camp."
  ([target] (strike target :fists)) ; arity 1 without weapon
  ([{:keys [camp armor], :or {armor 0}, :as target} weapon] ; uses or if key doesn't exist
   (let [points (weapon weapon-damage)]
     (if (= :gnomes camp)
       (update target :health + points)
       (let [damage (* points (- 1 armor))]
         (update target :health - damage))))))

(def enemy {:name "Zulkaz", :health 250, :armor 0.8, :camp :trolls})

(strike enemy :sword)
(strike enemy :cast-iron-saucepan)
(strike enemy)

(def ally {:name "Carla", :health 80, :camp :gnomes})

(strike ally :staff)

; because of code as data, data as code, a map can contain a function
; calling without args will return the function
; this is a VERY good alternative to a whole set of if statements to use the correct function based on value
(def weapon-fn-map {:fists (fn [health] (if (< health 100) (- health 10) health))
                    :staff (partial + 35)
                    :sword #(- % 100) ; partial won't work here because of params: ((partial - 100) 150) = -50
                    :cast-iron-saucepan #(- % 100 (rand-int 50))
                    :sweet-potato identity}) ; literaly returns input

(defn strike-using-weapon-fn
  ([target] (strike-using-weapon-fn target :fists))
  ([target weapon]
   (let [weapon-fn (weapon weapon-fn-map)]
     (update target :health weapon-fn))))

(-> enemy
    (strike-using-weapon-fn :sword)
    (strike-using-weapon-fn :sword)
    (strike-using-weapon-fn :cast-iron-saucepan))

(defn mighty-strike
  [target]
  (let [weapon-fn (apply comp (vals weapon-fn-map))] ; take the values of map, use as arguments to comp
    (update target :health weapon-fn)))

; Multi methods
(def player {:name "Lea" :health 200 :position {:x 10 :y 10 :facing :north}})


; (defmulti move #(:facing (:position %)))
; using functional composition:
(defmulti move (comp :facing :position))

(defmethod move :north
  [entity]
  (update-in entity [:position :y] inc))

(defmethod move :south
  [entity]
  (update-in entity [:position :y] dec))

(defmethod move :west
  [entity]
  (update-in entity [:position :x] inc))

(defmethod move :east
  [entity]
  (update-in entity [:position :x] dec))

(defmethod move :default [entity] entity)

(move player)

; test direction
(move {:position {:x 10 :y 10 :facing :west}})

; test default
(move {:position {:x 10 :y 10 :facing :wall}})