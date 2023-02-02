(ns puzzels.maximum-occurring-character)

; Complete the 'maximumOccurringCharacter' function below.
;
; The function is expected to return a CHARACTER.
; The function accepts STRING text as parameter.

(defn maximumOccurringCharacter
  "This trial is using common functions and coll's
   The filter step is extra work since we can escape on the first max-val"
  [text]
  (let [freq (frequencies text)
        max-val (apply max (vals freq))]
    (-> (filter #(= max-val (second %)) freq)
        (ffirst))))

(comment

  ; should be a
  (maximumOccurringCharacter "aabbcc")

  ; should be a
  (maximumOccurringCharacter "aabbccddeeffgghhiijjkkllmmnnooppqqrrssttuuvvwwxxyyzz")

  ; should be l
  (maximumOccurringCharacter "helloworld")

  ; should be i
  (maximumOccurringCharacter "BM5CiLi3VK3gDvMrm0spLKrsuObUFWyDWqwiEi8axIiyoED7w24PnVuBIf0XZzO6BEOg0pMz5XdcGKEaaWvGcbyAxkQyN1CdFjUX")
  )