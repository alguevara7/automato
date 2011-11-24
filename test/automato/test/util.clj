(ns automato.test.util
  (:use [automato.util])
  (:use [clojure.test])
  (:import (java.util Date)
           (java.util Calendar)))

(deftest test-date-range
  (let [start (date 1 1 2000)
        end (date 2 1 2000)]
    (is (= (date-range start end) [start end]))))

