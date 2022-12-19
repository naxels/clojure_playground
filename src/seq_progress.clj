(ns seq-progress)

;; https://stackoverflow.com/questions/2022911/idiomatic-clojure-for-progress-reporting

; libs
;; https://github.com/tmountain/seq-peek
;; https://github.com/cloojure/tupelo <- misc/dot
;; (ns xxx.core
;;   (:require [tupelo.misc :as tm]))
;; (tm/dots-config! {:decimation 10})
;; (tm/with-dots
;;   (doseq [ii (range 2345)]
;;     (tm/dot)
;;     (Thread/sleep 5)))

(def data-to-process (range 100))

(defn processing-fn
  [x]
  x)

(defn report [a]
  (println "Done " a)
  (+ 1 a))

(defn report-dot
  "needs to be a function"
  []
  (println "."))

(defn report-done
  []
  (println "Done"))

(defn seq-counter
  "calls callback after every n'th entry in sequence is evaluated. 
  Optionally takes another callback to call once the seq is fully evaluated."
  ([sequence n callback]
   (map #(do (when (= (rem %1 n) 0) (callback)) %2) (iterate inc 1) sequence))
  ([sequence n callback finished-callback]
   (drop-last (lazy-cat (seq-counter sequence n callback)
                        (lazy-seq (cons (finished-callback) ()))))))

; report-dot for every 5th progress
(map processing-fn (seq-counter data-to-process 5 report-dot))

; report dot for every 10th progress & report-done at the end
(map processing-fn (seq-counter data-to-process 10 report-dot report-done))


; example with agent
(let [reports (agent 0)]
  (map #(do (send reports report)
            (processing-fn %))
       data-to-process))