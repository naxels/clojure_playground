(ns clojure-workshop.associative-destruct)

(def mapjet-booking
  {:id 8773
   :customer-name "Alice Smith"
   :catering-notes "Vegetarian on Sundays"
   :flights [{:from {:lat 48.9615 :lon 2.4372 :name "Paris Le Bourget Airport"}
              :to {:lat 37.742 :lon -25.6976 :name "Ponta Delgada Airport"}}
             {:from {:lat 37.742 :lon -25.6976 :name "Ponta Delgada Airport"}
              :to {:lat 48.9615 :lon 2.4372 :name "Paris Le Bourget Airport"}}]})

(let [{:keys [customer-name flights]} mapjet-booking]
  (println (str customer-name " booked " (count flights) " flights.")))

(defn print-flight-overview
  [lat1 lon1 lat2 lon2]
  (println (str "Flying from: Lat " lat1 " Lon " lon1 " Flying to: Lat " lat2 " Lon " lon2)))

(defn print-mapjet-flight
  [flight]
  (let [{:keys [from to]} flight
        {lat1 :lat lon1 :lon} from
        {lat2 :lat lon2 :lon} to]
    (print-flight-overview lat1 lon1 lat2 lon2)))

(defn print-mapjet-flight-associate-destruct
  [flight]
  (let [{{lat1 :lat lon1 :lon} :from,
        {lat2 :lat lon2 :lon} :to} flight]
    (print-flight-overview lat1 lon1 lat2 lon2)))

(defn print-mapjet-flight-arg-destruct
  "takes a flight and destructs it"
  [{{lat1 :lat lon1 :lon} :from
    {lat2 :lat lon2 :lon} :to}]
  (print-flight-overview lat1 lon1 lat2 lon2))

(defn print-flight-vec
  [[[lat1 lon1] [lat2 lon2]]] ; destruct a vec, surrounded by the total arg's vec
  (print-flight-overview lat1 lon1 lat2 lon2))

(print-flight-vec [[48.9615, 2.4372], [37.742 -25.6976]])

(map print-mapjet-flight-arg-destruct (:flights mapjet-booking))

(comment
  (let [x (first (:flights mapjet-booking))]
    (print-mapjet-flight x)
    (print-mapjet-flight-associate-destruct x))
  )