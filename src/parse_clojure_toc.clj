(ns parse-clojure-toc
  (:require [clojure.java.io :as io]
            [clojure.xml :as xml]
            [clojure.inspector :refer [inspect-tree]]))
;   (:require [clojure.data.xml :refer [parse-str]]))

; NOTE:
; Alex Miller: clojure.xml is a very old stab from early in the Clojure language and 
; should be considered deprecated at this point

; (def data
;   (-> "Clojure_The_Essential_Reference_v29_MEAP.xml"
;       slurp
;       parse-str
;       xml-seq))
      
; (def document (xml/parse (io/reader "Clojure_The_Essential_Reference_v29_MEAP.xml")))
(def document (xml/parse "Clojure_The_Essential_Reference_v29_MEAP.xml"))

(def chapters
  (->> document
       xml-seq
       (filter (comp #{:navPoint} :tag))
    ;    (filter (fn [el] (#{:navLabel} (:tag el)))))) ; without comp
    ;    (filter #((#{:navLabel} (:tag %)))))) ; anonymous without comp
    ;    (filter (comp #{:navLabel} :tag))
       (map :content) ; returns LazySeq of Vectors
       (map first) ; works but not data driven
       ;(filter (comp #{:navLabel} :tag))
       (map :content)
       (map first)
       (map :content)
       (map first)
       ))

; list of each clojure function in core:
; (ns-publics 'clojure.core)

(def chapters-with-digits
  (filter #(re-find #"^\d" %) chapters))

(comment
  (inspect-tree document)
  (inspect-tree chapters))