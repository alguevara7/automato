(ns automato.test.util
  (:use [automato.util])
  (:use [clojure.test])
  (:import (java.util Date)))

(defn date [date-as-text]
  (Date. date-as-text))

(deftest test-date-range
  (is (= (date-range (date "2011/01/01") (date "2009/01/01")) []) "No tests have been written."))

