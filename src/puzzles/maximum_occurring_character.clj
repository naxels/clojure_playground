(ns puzzles.maximum-occurring-character)

;; HackerRank

;; Given a string, return the character that appears the maximum number of times in the string. The string will contain only ASCII characters, from the ranges ('a'-'z','A'-'Z','0'-'9'), and case matters. If there is a tie in the maximum number of times a character appears in the string, return the character that appears first in the string.
 
;; Example
;; text = abbbaacc
 
;; Both 'a' and 'b' occur 3 times in text.  Since 'a' occurs earlier, a is the answer.
 
;; Function Description
 
;; Complete the function maximumOccurringCharacter in the editor below.
 
;; maximumOccurringCharacter has the following parameter:
;;     string text:  the string to be operated upon
 
;; Returns
;;     char : The most occurring character that appears first in the string.

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