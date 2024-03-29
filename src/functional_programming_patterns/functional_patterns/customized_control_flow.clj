(ns functional-programming-patterns.functional-patterns.customized-control-flow)

(defmacro make-logger [name form]
  `(defn ~name []
     (println (str "Calling " ~name))
     ~form))

(defn choose [num first second third]
  (cond
    (= 1 num) (first)
    (= 2 num) (second)
    (= 3 num) (third)))

(defmacro simpler-choose [num first second third]
  `(cond
     (= 1 ~num) ~first
     (= 2 ~num) ~second
     (= 3 ~num) ~third))

(defn time-run [to-time]
  (let [start (System/currentTimeMillis)]
    (to-time)
    (- (System/currentTimeMillis) start)))

(defmacro avg-time [times to-time]
  `(let [total-time#
         (apply + (for [_# (range ~times)] (time-run (fn [] ~to-time))))]
     (float (/ total-time# ~times))))

(defn avg-time-fn [times to-time]
  (let [total-time
        (apply + (for [_ (range times)] (time-run to-time)))]
    (float (/ total-time times))))

(let
 [total-time
  (apply + (for [_ (range 5)] (time-run (fn [] (Thread/sleep 100)))))]
  (float (/ total-time 5)))

; -----
(avg-time 5 (Thread/sleep 1000))

(macroexpand (avg-time 5 (Thread/sleep 1000)))

(macroexpand-1 (avg-time 5 (Thread/sleep 1000)))

(choose 1 #() #() #())

; returns fn
(simpler-choose 1 #() #() #())