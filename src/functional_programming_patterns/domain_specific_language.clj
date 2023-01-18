(ns functional-programming-patterns.domain-specific-language
  (:require [clojure.string :as str]
            [clojure.java.shell :as shell]))

(defn- print-output
  [output]
  (println (str "Exit Code: " (:exit output)))
  (when-not (str/blank? (:out output)) (println (:out output)))
  (when-not (str/blank? (:err output)) (println (:err output)))
  output)

(defn- print-output-destruct
  "Private defn with destructuring the expected keys,
   a little clearer to me & get keys from map just once
   Returns full input
   -Personally I would choose a different fn name & arg"
  [{:keys [exit out err] :as output}]
  (println (str "Exit Code: " exit))
  (when-not (str/blank? out) (println out))
  (when-not (str/blank? err) (println err))
  output)

;; (defn command [command-str]
;;   (let [command-parts (str/split command-str #"\s+")]
;;     (fn []
;;       (print-output (apply shell/sh command-parts)))))

(defn command
  "Returns a function"
  [command-str]
  (let [command-parts (str/split command-str #"\s+")]
    (fn
      ([] (print-output (apply shell/sh command-parts)))
      ([{old-out :out}]
       (print-output (apply shell/sh (concat command-parts [:in old-out])))))))

(defn pipe
  [commands]
  (apply comp (reverse commands)))

(defn pipe-commands
  "Takes args, applies command & then comp's"
  [& commands]
  (apply comp (map command (reverse commands))))

(defmacro def-command [name command-str]
  `(def ~name ~(command command-str)))

(defmacro def-pipe [name & command-strs]
  (let [commands (map command command-strs)
        pipe (pipe commands)]
    `(def ~name ~pipe)))

; -----

(print-output [])
(print-output {:exit 1})

((command "ls"))

(def ls (command "ls -la"))
(ls)

(print-output-destruct [])
(print-output-destruct {:exit 1})

;; (pipe ["ls" "du"])

(def grep-readme-from-ls
  (pipe [(command "ls")
         (command "grep README")]))

(grep-readme-from-ls)

((pipe-commands "ls" "grep README"))

(def-command pwd "pwd")

(pwd)

(def-pipe pipeline "ls" "ls -lha")
(pipeline)