(ns automato.test.core
  (:use [automato.core]
        [clojure.test])
  (:require [automato.util :as util]))

(deftest test-generate-burn-up-chrt-data-points
  (is (= (generate-burn-up-chart-data-points (util/date 1 10 2011) (util/date 2 10 2011) [])
         [{(util/date 1 10 2001) 0, (util/date 2 10 2001) 0}])
      "blah."))

