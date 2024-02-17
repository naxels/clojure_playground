(ns tree-seq
  (:require [clojure.data.xml :as xml]
            [clojure.java.io :as io]
            [clojure.pprint]
            [data]))

;; xml-seq is a call to tree-seq and does a full unroll of all the nodes
(def parsed-xml (-> data/xml-example .getBytes io/input-stream xml/parse))

; working directly with the parsed-xml just gives the data tree
; containing :tag, :content :attrs
(count parsed-xml) ; 3
(keys parsed-xml)

; getting the contractId would require to traverse the entire tree
(-> parsed-xml
    (:content)
    ; find :contract :tag
    (:content)
    ; find :contractId :tag
    )

; however xml-seq turns it into a full unroll of the tree, making it easier to work with
; the data, unnesting the sub sections
(def seqd-xml (xml-seq parsed-xml))

(count seqd-xml) ; 17
;; (keys seqd-xml) ; fails since it's a seq now

; now it's easy to get the contractId as if there is no nesting
(filter #(= :contractId (:tag %)) seqd-xml)

;; tree-seq
; a branch? is the vector? test
; and the children is the identity (self)
(clojure.pprint/pprint
 (tree-seq
  vector? ; branch?
  identity ; children
  [[1 2 [3 [[4 5] [] 6]]]]))

(clojure.pprint/pprint
 (tree-seq
  map? ; branch?
  last ; children - note that identity doesn't unroll :c like last does due to the map? test
  data/coll-map-nested))
