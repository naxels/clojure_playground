(ns clojure-workshop.in-memory)

(def memory-db (atom {}))

(defn read-db [] @memory-db)

(defn write-db [new-db] (reset! memory-db new-db))

; setup
(write-db {:clients {:data [{:id 1 :name "Bob" :age 30} {:id 2 :name "Alice" :age 24}]
                     :indexes {:id {1 0, 2 1}}}
           :fruits {:data [{:name "Lemon" :stock 10} {:name "Coconut" :stock 3}]
                    :indexes {:name {"Lemon" 0, "Coconut" 1}}}
           :purchases {:data [{:id 1 :user-id 1 :item "Coconut"} {:id 1 :user-id 2 :item "Lemon"}]
                       :indexes {:id {1 0, 2 1}}}})

; note: functions have side effects!
; indexes = key + position in Map

(defn create-table
  [table-name]
  (write-db (conj (read-db) {(keyword table-name) {:data [] :indexes {}}})))

(defn delete-table
  [table-name]
  (write-db (dissoc (read-db) (keyword table-name)))
  )

(defn select-*
  "Return all the records of a table passed as a parameter"
  [table-name]
  (get-in (read-db) [(keyword table-name) :data] "Not found"))

;(select-*-where :fruits :name "Lemon")
; (defn select-*-where
;   "The function should use the index map to retrieve the index of the record 
;    in the data vector and return the element.
;    TODO: when key not found.."
;   [table-name field field-value]
;   (let [index (get-in (read-db) [(keyword table-name) :indexes field field-value])]
;     ((get-in (read-db) [(keyword table-name) :data]) index)))

(defn select-*-where ; taken from solution
  [table-name field field-value]
  (let [db (read-db)
        index (get-in db [table-name :indexes field field-value])
        data (get-in db [table-name :data])]
    (get data index)))

;(insert :fruits {:name "Pear" :stock 3} :name)
(defn insert ; taken from solution
  [table-name record id-key]
  (if-let [_existing-record (select-*-where table-name id-key (id-key record))]
    (println (str "Record with " id-key ": " (id-key record) " already exists. Aborting"))
    (let [db (read-db)
          new-db (update-in db [table-name :data] conj record) ; it's a vec of maps, so conj to add record
          index (- (count (get-in new-db [table-name :data])) 1)] ; because coll starts at 0
      (write-db
       (update-in new-db [table-name :indexes id-key] assoc (id-key record) index)))))