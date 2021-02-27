(ns functional-thinking.5
  (:require [clojure.data.xml :refer [parse]]
            ;[clojure.xml :refer [parse]]
            [clojure.java.io :as io]
            [clojure.test :refer [is]]))

; uri was updated
; param w was updated to woeid
; uri requires OAuth 1 authentication using Yahoo key now

(def WEATHER-URI
  "https://weather-ydn-yql.media.yahoo.com/forecastrss?woeid=%d&u=f")


(defn get-xml-from-uri
  [city-code]
  (xml-seq (parse (format WEATHER-URI city-code))))

; file based approach that is working
(def FILENAME-SAMPLE "yahoo-weather-response.xml")

; load the XML file and parse it
(defn load-xml-resource [file-path]
  (-> file-path
      io/resource
      io/reader
      parse))

; turn the xml file into a seq
(defn get-xml-from-file
  [_city-code]
  (xml-seq (load-xml-resource FILENAME-SAMPLE)))

; the yweather:(name) is turned into a fully qualified name
; from xlmns:yweather:=""
(def xml-ns "xmlns.http%3A%2F%2Fxml.weather.yahoo.com%2Fns%2Frss%2F1.0")

(defn qualify [kw]
  (keyword xml-ns (name kw)))

;; (qualify :location)
;; => :xmlns.http%3A%2F%2Fxml.weather.yahoo.com%2Fns%2Frss%2F1.0/location


; get the location
(defn get-location
  [city-code]
  (for [x (get-xml-from-file city-code)
        ;; :when (= :yweather:location (:tag x))
        :when (= (qualify :location) (:tag x))]
    (str (:city (:attrs x)) "," (:region (:attrs x)))))

; get the temperature
(defn get-temp
  [city-code]
  (for [x (get-xml-from-file city-code)
        ;; :when (= :yweather:condition (:tag x))
        :when (= (qualify :condition) (:tag x))]
    (:temp (:attrs x))))

(get-location 12770744)
(get-temp 12770744)


;; --------------------------- LETTER GRADES
(defn in
  [score low high]
  (and (number? score) (<= low score high)))

(defn letter-grade
  [score]
  (cond
    (in score 90 100) "A"
    (in score 80 90) "B"
    (in score 70 80) "C"
    (in score 60 70) "D"
    (in score 0 60) "F"
    (re-find #"[ABCDFabcdf]" score) (.toUpperCase score)))

(letter-grade 55)
(letter-grade 85)
(letter-grade "F")
(letter-grade "G")
(letter-grade "b")

; dorun allows sideeffects to occur
; test number to letter:
(dorun (map #(is (= "A" (letter-grade %))) (range 90 100)))
(dorun (map #(is (= "B" (letter-grade %))) (range 80 90)))
; test string to uppercase
(dorun (map #(is (= (.toUpperCase %) (letter-grade %))) ["A" "B" "C" "D" "E" "F" "a" "b" "c" "d" "e" "f"]))
; test not found letter
(is (= nil (letter-grade "G")))