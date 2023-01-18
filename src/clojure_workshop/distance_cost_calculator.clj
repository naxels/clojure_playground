(ns clojure-workshop.distance-cost-calculator
  (:require [clojure.test :refer [is]]))

(def walking-speed 4) ; exercise says 5 however all tests are setup for 4

(def driving-speed 70)

; used to calculate the kilometers * type of vehicle
(def vehicle-cost-fns
  {:sporche (partial * 0.12 1.5)
   :tayato (partial * 0.07 1.3)
   :sleta (partial * 0.2 0.1)})

;; test lat lon
(def paris {:lat 48.856483 :lon 2.352413})

(def bordeaux {:lat 44.834999  :lon -0.575490})

(def london {:lat 51.507351, :lon -0.127758})

(def manchester {:lat 53.480759, :lon -2.242631})

;; functions
(defn distance
  "Returns a rough estimate of the distance between two coordinate points, in kilometers. Works better with smaller distance"
  [{lat1 :lat lon1 :lon} {lat2 :lat lon2 :lon}]
  (let [deglen 110.25
        x (- lat2 lat1)
        y (* (Math/cos lat2) (- lon2 lon1))]
    (* deglen (Math/sqrt (+ (* y y) (* x x))))))

(defmulti itinerary
  "Calculate the distance of travel between two location, and the cost and duration based on the type of transport"
  :transport) ; :transport is dispatch

(defmethod itinerary :walking
  [{from :from to :to}]
  (let [distance (distance from to)
        duration (/ distance walking-speed)]
  {:cost 0 :distance distance, :duration duration}))

(defmethod itinerary :driving
  [{from :from to :to vehicle :vehicle}] ;;;;; (itinerary {:from manchester :to london :transport :driving :vehicle :sleta})
  (let [distance (distance from to)
        duration (/ distance driving-speed)
        cost ((vehicle vehicle-cost-fns) distance)]
   {:cost cost :distance distance, :duration duration}))

;; test cases
(is (= {:cost 0, :distance 491.61380776549225, :duration 122.90345194137306}
     (itinerary {:from paris :to bordeaux :transport :walking})))

(is (= {:cost 44.7368565066598, :distance 491.61380776549225, :duration 7.023054396649889}
     (itinerary {:from paris :to bordeaux :transport :driving :vehicle :tayato})))

(is (= {:cost 0, :distance 318.4448148814284, :duration 79.6112037203571}
     (itinerary {:from london :to manchester :transport :walking})))

(is (= {:cost 4.604730845743489, :distance 230.2365422871744, :duration 3.2890934612453484}
     (itinerary {:from manchester :to london :transport :driving :vehicle :sleta})))