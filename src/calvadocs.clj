(ns calvadocs)

(comment ; rich comment block
  (+ (* 2 2) ; execute current form: ctrl+enter
     2) ; execute current top form: alt+enter
  (Math/abs -1)
  (defn hello [s]
    (str "Hello " s))
  (hello "Calva REPL")
  (range 10)
  "I ♥️ Clojure")