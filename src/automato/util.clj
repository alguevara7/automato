(ns automato.util
  (:use [clojure.core]
        [clojure.contrib.properties]
        [clojure.java.io])
  (:require [clojure.string :as str]
            [clojure.java.jdbc :as sql]
            [clojure.contrib.seq :as seq1])
  (:import (java.util Date)))


(defn date-range []
  [(Date.)])


;(date-range (date "2011/11/01") (date "2011/12/06") )