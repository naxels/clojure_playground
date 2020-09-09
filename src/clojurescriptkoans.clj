(ns clojurescriptkoans)

(= [[:top :left] [:top :middle] [:top :right] [:middle :left] [:middle :middle] [:middle :right] [:bottom :left] [:bottom :middle] [:bottom :right]] 
   (for [row [:top :middle :bottom] 
         column [:left :middle :right]]
     [row column]))

(= [true false true] 
   (let [not-a-symbol? (complement symbol?)]
     (map not-a-symbol? [:a 'b "c"])))

; Praise and 'complement' may help you separate the wheat from the chaff
(= [:wheat "wheat" 'wheat] 
   (let [not-nil? (complement nil?)] 
     (filter not-nil? [nil :wheat nil "wheat" nil 'wheat nil])))