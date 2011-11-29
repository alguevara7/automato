(ns automato.test.core
  (:use [automato.core]
        [automato.util]
        [clojure.test])
  (:require [automato.util]))

(deftest test-generate-burn-up-chrt-data-points
  (is (= (data-points
          (date 1 10 2011)
          (date 10 10 2011)
          [{:completed (date  1 10 2011) :size "5"}
           {:completed (date  5 10 2011) :size "3"}
           {:completed (date  8 10 2011) :size "1"}
           {:completed (date 10 10 2011) :size "1"}])
         [5 5 5 5 8 8 8 9 9 10])
      "blah."))

(deftest test-add-to-all
  (let [date1 (date 1 0 2000)
        date2 (date 2 0 2000)
        date3 (date 3 0 2000)
        date4 (date 4 0 2000)]
    (is (= (add-to-all date1 date4 5 {date1 1, date2 2, date3 3})
         {date1 6, date2 7, date3 8, date4 5})
      "add to all values")))

