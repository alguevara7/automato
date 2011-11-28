(ns automato.test.core
  (:use [automato.core]
        [clojure.test])
  (:require [automato.util :as util]))

(deftest test-generate-burn-up-chrt-data-points
  (is (= (generate-burn-up-chart-data-points (util/date 1 10 2011) (util/date 2 10 2011) [])
         [{(util/date 1 10 2001) 0, (util/date 2 10 2001) 0}])
      "blah."))

(deftest test-add-to-all
  (is (= (add-to-all (util/date 1 0 2000) (util/date 2 0 2000) 1 {(util/date 1 0 2000) 1, (util/date 2 0 2000) 1})
         [{(util/date 1 0 2000) 2, (util/date 2 0 2000) 2}])
      "add to all values"))

