(ns hacking-the-blog-categories.set-to-map
  (:require data))

; https://jmglov.net/blog/2022-07-06-hacking-blog-categories.html

; Goal: group by category

(def posts (sort-by :date (comp - compare) data/posts))

; now group by :categories

; online solution
; interesting note: first (sort-by :date) to reset the sorting while ultimately it's again sorted correctly

;; The mapcat step takes each post
;; and maps over the :categories list (that {:keys [categories]} bit is key destructuring, if you haven't seen it before), 
;; turning each category into a tuple of [category post]. For this specific post, this would yield:

;; Each post is turned inside out, yielding a list of lists of tuples, 
;; or at least before the "cat" part of mapcat goes to work. 
;; The difference between map and mapcat is that mapcat flattens the resulting list

(def posts-by-category
  (->> posts
       (sort-by :date) ; needed for ultimately getting the correct sort
       (mapcat (fn [{:keys [categories] :as post}]
                 (map (fn [category] [category post]) categories)))
       (reduce (fn [acc [category post]]
                 (update acc category #(conj % post)))
               {})))

;; (posts-by-category posts) ; don't understand why it's not a function..
posts-by-category

(keys posts-by-category)

; -- broken down in pieces

(def posts-step1-sorted
  (sort-by :date posts))

(def posts-step2-mapcat
  (mapcat (fn [{:keys [categories] :as post}]
            (map (fn [category] [category post]) categories))
          posts-step1-sorted))

(def posts-step-3-reduce
  (reduce (fn [acc [category post]]
            #_(assoc acc category (conj (get acc category) post)) ; the same as update but with a lookup and a re-associate
            (update acc category #(conj % post)))
          {}
          posts-step2-mapcat))

; sort on keys
(sort-by first posts-step2-mapcat)

posts-step-3-reduce
(keys posts-step-3-reduce)
(map #(count (second %)) posts-step-3-reduce)

; TRIALS

; STEP 1
; group-by won't work on the list of maps as it will take the set of data as key
(group-by :categories posts-step1-sorted)

; doesn't work as the :categories set needs to be fully used, not just 1 element
(group-by (fn [x] (second (:categories x))) posts-step1-sorted)
(group-by (fn [x] (map identity (:categories x))) posts-step1-sorted)

; TO TRY
; FROM SLACK
; I might have gone strait to a map as well, and then apply merge-with into
; so construct a bunch of maps like {category [post]}  and then merge them
; my idea:
; transformation:
; list of maps -> list of maps with {category [posts]} -> apply merge-with -> map of category [posts]
; NOT DONE YET..

; STEP 2&3 in 1 go
(reduce (fn [acc {:keys [categories] :as post}]
          #_(loop [[category & categories] categories] ; doesn't work as assoc/return isn't the last call of the loop
              (when category
                (do (assoc acc category post)
                    (recur categories))))
          #_(let [category (first categories)] ; only takes first category
              (assoc acc category post))
          #_(loop [category (first categories)] ; endless loop
              (println category)
              (when category
                (do
                  (assoc acc category post)
                  (recur (next categories))))))
        {}
        posts-step1-sorted)

; turn 1 post into a dataset of each category found in set containing the post
(let [post (first posts-step1-sorted)]
  (println (first (:categories post)))
  (map (fn [category] [category post]) (:categories post)))


; STEP 2
; SLACK response:
; @hiredman: a map inside a mapcat is a for
(for [{:keys [categories] :as post} posts-step1-sorted
      category categories]
  [category post])

; STEP 3
; group-by won't work perfectly on first, as it adds the entire vector [key map]
(group-by first posts-step2-mapcat)

; trial with destruct the vector [string map] - same result as just doing first
(group-by (fn [[category _post]] category) posts-step2-mapcat)

(first posts-step2-mapcat)

; Clouncil discussion of the problem July 27, 2022

; Arne: cartesian product, since we need Y categories of X posts, so Y*X
; he would use (for.. since it does just that

; reduce in reduce:
(reduce
 (fn [acc {:keys [categories] :as post}]
   (reduce
    (fn [acc category]
      (update acc category conj post))
    acc
    categories))
 {}
 posts)

; use into with a map + filter on a transformed dataset (unique list of categories)
(let [categories (distinct (mapcat :categories posts))]
  (into {}
        (map (fn
               [category]
               [category (filter #(contains? (:categories %) category) posts)])
             categories)))

; get all categories
(mapcat :categories posts)
; get the unique categories
(distinct (mapcat :categories posts))

; gets you really close as well:
(->> posts
     (sort-by :date) ; needed for ultimately getting the correct sort
     (mapcat (fn [{:keys [categories] :as post}]
               (map (fn [category] [category post]) categories)))
     (group-by first))

; using for to turn it into a vector of category with post
(for [post posts
      category (:categories post)]
  [category post])

; for + group-by on the vals
(update-vals
 (group-by first
           (for [post posts
                 category (:categories post)]
             [category post]))
 #(map second %))

; reduce the for (val) into the map
(reduce
 (fn [acc [cat post]]
   (update acc cat conj post))
 {}
 (for [post posts
       category (:categories post)]
   [category post]))

; using juxt / comp
(into {} 
      (reduce (partial merge-with concat)
              (map (comp (partial apply zipmap)
                         (juxt :categories (comp repeat list)))
                   posts)))
