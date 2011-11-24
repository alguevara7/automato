(ns automato.util
  (:use [clojure.core]
        [clojure.contrib.properties]
        [clojure.java.io])
  (:require [clojure.string :as str]
            [clojure.java.jdbc :as sql]
            [clojure.contrib.seq :as seq1])
  (:import (java.util Date)
           (java.util Calendar)))

(defn spy [value]
  (do (println value)
      value))

(defn date [day month year]
  (let [calendar (Calendar/getInstance)]
    (do
      (.set calendar (Calendar/DAY_OF_MONTH) day)
      (.set calendar (Calendar/MONTH) month)
      (.set calendar (Calendar/YEAR) year)
      (.getTime calendar))))

(defn date-range [start end]
  (vec (take-while (fn [date] (<= (.getTime date) (.getTime end))) (date-lazy-seq start))))

;use tail recursion
(defn- date-lazy-seq [start]
  (let [calendar (Calendar/getInstance)]
    (.setTime calendar start)
    (lazy-seq (cons (.getTime  calendar) (date-lazy-seq (do (.add calendar (Calendar/DAY_OF_MONTH) 1) (.getTime calendar)))))))