(ns functional-programming-for-oo.scheduling)

;; https://github.com/marick/fp-oo/tree/master/sources

;; ch7: 
;; shows how instead of using if's and other control statements
;; to direct the flow of data, you add data to the dataset (transformation)
;; then apply a higher order functions to just filter/do transformations on the relevant data
;; also a great display of how several functions come together to transform the data into it's final form
;; modeled around the domain

(def data
  [{:course-name "Zigging"
    :morning? true
    :registered 5
    :spaces-left 2
    :already-in? true
    :limit 7}
   {:course-name "Zagging"
    :morning? false
    :registered 1
    :spaces-left 0
    :already-in? false
    :limit 1}])

(defn answer-annotations
  [courses registrants-courses]
    (let [checking-set (set registrants-courses)] ; get the unique registrants-courses
      (map (fn [course]
             (assoc course
                    :spaces-left (- (:limit course)
                                    (:registered course))
                    :already-in? (contains? checking-set
                                            (:course-name course))))
           courses)))

(answer-annotations [{:course-name "zigging" :limit 4, :registered 3}
                     {:course-name "zagging" :limit 1, :registered 1}]
                    ["zagging"])

(answer-annotations data ["Zigging"])

(defn domain-annotations
  [courses]
  (map (fn [course]
         (assoc course
                :empty? (zero? (:registered course))
                :full? (zero? (:spaces-left course))))
       courses))

(domain-annotations [{:registered 1, :spaces-left 1},
                     {:registered 0, :spaces-left 1},
                     {:registered 1, :spaces-left 0}])

(defn note-unavailability
  [courses instructor-count]
  (let [out-of-instructors?
        (= instructor-count
           (count (filter (fn [course] (not (:empty? course)))
                           courses)))]
    (map (fn [course]
           (assoc course
                  :unavailable? (or (:full? course)
                                    (and out-of-instructors?
                                         (:empty? course)))))
         courses)))

(defn annotate
  [courses registrants-courses instructor-count]
  (-> courses
      (answer-annotations registrants-courses)
      domain-annotations
      (note-unavailability instructor-count)))

(annotate data ["Zigging"] 2)

(defn separate
  "Separate the coll on predicate, returns vec of seq's"
  [predicate coll]
  [(filter predicate coll) (remove predicate coll)])

(separate odd? [1 2 3 4 5])

; capturing the 2 data set with let, very verbose
(let [both (separate odd? [1 2 3 4 5])
      _odds (first both)
      evens (second both)]
  evens)

; capturing the 2 data sets with destructuring
(let [[_odds evens] (separate odd? [1 2 3 4 5])]
  evens)

(defn visible-courses
  "Show only available courses"
  [courses]
  (let [[guaranteed possibles] (separate :already-in? courses)]
    (concat guaranteed (remove :unavailable? possibles))))

(defn final-shape
  "for each course data map, only return the desired keys as a list of course maps"
  [courses]
  (let [desired-keys [:course-name :morning? :registered :spaces-left :already-in?]]
    (map (fn [course]
           (select-keys course desired-keys))
         courses)))

(final-shape data)

(defn half-day-solution
  [courses registrants-courses instructor-count]
  (-> courses
      (annotate registrants-courses instructor-count)
      visible-courses
      ((fn [courses] (sort-by :course-name courses))) ; anonymous function needed as sort-by doesn't take the coll as first argument
      final-shape))

(defn solution
  [courses registrants-courses instructor-count]
  (map (fn [courses]
         (half-day-solution courses registrants-courses instructor-count))
       (separate :morning? courses)))

(solution data ["Zigging"] 2) ; just show Zigging, course where you are already in
(solution data ["Zagging"] 2) ; show Zagging, course you are in and show Zigging as option to register for