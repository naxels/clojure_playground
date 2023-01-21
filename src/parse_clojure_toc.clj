(ns parse-clojure-toc
  (:require [clojure.data.xml :refer [parse-str alias-uri]]
            [clojure.inspector :refer [inspect-tree]]))

(defn load-xml-resource [file-path]
  (-> file-path
      (slurp)
      (parse-str)))

; the TOC is namespaced, so define it:
(alias-uri 'xh "http://www.daisy.org/z3986/2005/ncx/")

(def document (load-xml-resource "Clojure_The_Essential_Reference_v31_MEAP.xml"))

(def chapters
  (->> document
       (xml-seq)
       (filter (comp #{::xh/text} :tag))
       (map (comp first :content))))

(def chapters-with-digits
  (filter #(re-find #"^\d" %) chapters))

(comment
  (inspect-tree document)
  (inspect-tree chapters)
  (inspect-tree chapters-with-digits)

  ::xh/navPoint
  )