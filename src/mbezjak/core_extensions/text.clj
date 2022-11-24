(ns mbezjak.core-extensions.text
  "Missing functions in `clojure.string`.

  This namespace deals with Java and Clojure strings, but is not named `string`
  in order to avoid the clash with `clojure.string` namespace.

  Argument names and expected positions use the same convention as `clojure.string`."
  (:refer-clojure :exclude [double? int?])
  (:require
   [clojure.string :as string])
  (:import
   (java.net MalformedURLException URL)
   (java.time DateTimeException LocalDate Year YearMonth)))

(defn try-as-int
  "Try parsing `text` as an integer.

  Returns `nil` on failure."
  [text]
  (try
    (Integer/parseInt text)
    (catch NumberFormatException _
      nil)))

(defn try-as-double
  "Try parsing `text` as a double.

  Returns `nil` on failure."
  [text]
  (when text
    (try
      (Double/parseDouble text)
      (catch NumberFormatException _
        nil))))

(defn try-as-finite-double
  "Try parsing `text` as a finite double.

  Returns `nil` on failure. Rejects infinities and not-a-number."
  [text]
  (when-not (#{"NaN" "Infinity" "-Infinity"} (some-> text string/trim))
    (try-as-double text)))

(defn try-as-boolean
  "Try parsing `text` as a boolean.

  Returns `nil` on failure."
  [text]
  (let [t (some-> text string/trim)]
    (cond
      (= "true" t) true
      (= "false" t) false
      :else nil)))

(defn try-as-year
  "Try parsing `text` as a `java.time.Year`.

  Returns `nil` on failure."
  [text]
  (when text
    (try
      (Year/parse text)
      (catch DateTimeException _
        nil))))

(defn try-as-year-month
  "Try parsing `text` as a `java.time.YearMonth`.

  Returns `nil` on failure."
  [text]
  (when text
    (try
      (YearMonth/parse text)
      (catch DateTimeException _
        nil))))

(defn try-as-date
  "Try parsing `text` as a `java.time.LocalDate`.

  Returns `nil` on failure."
  [text]
  (when text
    (try
      (LocalDate/parse text)
      (catch DateTimeException _
        nil))))

(defn int? [text]
  (boolean (try-as-int text)))

(defn double? [text]
  (boolean (try-as-finite-double text)))

(defn positive-double? [text]
  (let [number (try-as-finite-double text)]
    (boolean (and number (pos? number)))))

(defn year? [text]
  (boolean (try-as-year text)))

(defn year-month? [text]
  (boolean (try-as-year-month text)))

(defn date? [text]
  (boolean (try-as-date text)))

(defn url? [text]
  (try
    (URL. text)
    true
    (catch MalformedURLException _
      false)))

(defn as-number-if-possible
  "Best effort coerse `text` into a number or simply return `text` if not possible.

  Will try to return the most restrictive number type possible. E.g. an integer
  instead of returning a double. Infinities and not-a-number are not considered
  as numbers."
  [text]
  (or (try-as-int text)
      (try-as-finite-double text)
      text))

(defn ellipsis
  "Truncate extra characters from `text` with ..."
  [text max-length]
  (if (<= (count text) max-length)
    text
    (let [ellipsis "..."
          up-to (max 0 (- max-length (count ellipsis)))]
      (-> text
          (subs 0 up-to)
          (str ellipsis)
          (subs 0 max-length)))))

(defn blank->empty [text]
  (if (string/blank? text)
    ""
    text))