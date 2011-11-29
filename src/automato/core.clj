(ns automato.core
  (:use [clojure.core]
        [automato.util]
        [hiccup.core]
        [hiccup.page-helpers]
        [clojure.contrib.properties]
        [clojure.java.io]
        [clojure.contrib.command-line]
        [clojure.contrib.generic.functor])
  (:require [automato.db :as db]
            [clojure.string :as str]
            [clojure.java.jdbc :as sql]
            [clojure.contrib.seq :as seq1])
   (:import (java.net URI)
           (java.util Properties)
           (java.util Calendar)
           (java.util Date)))

(defn calculate-percent-done [chapters]
  (if (seq chapters)
    (* (/ (count (filter (fn [chapter] (:completed chapter)) chapters))
          (count chapters))
       100)
    0))

(defn budget-used [chapter]
  (if (nil? (:budget_used chapter))
    0
    (:budget_used chapter)))

(defn calculate-percent-budget-used [story chapters]
  (if (:size story)
    (* (/ (reduce + 0 (map (fn [chapter] (budget-used chapter)) chapters))
          (* (read-string (:size story)) 80.0))
       100)
    0))

(defn budget-alert [percent-budget-used percent-done threshold]
  (if (or (and (> percent-budget-used threshold) (< percent-done threshold))
          (> percent-budget-used 100))
    (html [:span {:style "color:red;font-weight:bold"} "Yes"])
    (html [:span {:style "color:green;font-weight:normal"} "No"])))

(defn chapter-done [chapter]
  (if (:completed chapter)
    (html [:span {:style "color:green;font-weight:bold"} "Yes"])
    (html [:span {:style "color:blue;font-weight:normal"} "No"])))

(defn- line [max-data-points goal]
  (str/join ","
            (map
             (fn [[index, value]] (str (float (* index value))))
             (seq1/indexed (repeat max-data-points (/ goal max-data-points))))))

(defn burn-up-chart [data-points max-data-points goal]
  (image (url "http://chart.apis.google.com/chart"
        {:chs "600x250"
         :chtt "burnup Chart"
         :cht "lxy"
         :chdl "estimated|actual"
         :chco "FF0000,00FF00"
         :chxr (str "0,0," max-data-points ",1|1,0," goal ",1")
         :chds (str "0," goal)
         :chd (str "t:"
                   (str/join "," (map (fn [index] (str index)) (range 0 max-data-points))) "|"
                   (line max-data-points goal) "|"
                   (str/join "," (map (fn [index] (str index)) (range 0 max-data-points))) "|"
                   (str/join "," data-points)
                   )})))

(defn wipe-time [date-time]
  (let [calendar (Calendar/getInstance)]
    (do
      (.setTime calendar date-time)
      (.set calendar (Calendar/HOUR_OF_DAY) 0)
      (.set calendar (Calendar/MINUTE) 0)
      (.set calendar (Calendar/SECOND) 0)
      (.set calendar (Calendar/MILLISECOND) 0)
      (.getTime calendar))))

(defn generate-burn-up-chart-data-points [start-date end-date chapters]
  (let [date-to-points-done (fmap (fn [chapters-completed-on-same-day]
                                    (reduce + 0 (map #(read-string (:size %)) chapters-completed-on-same-day)))
                                  (group-by (fn [chapter] (:completed chapter))
                                            chapters))]
    (into [] (for [date (date-range start-date end-date)]
               (reduce (fn [sum d] (+ sum (if-let [points (get date-to-points-done d)] points 0))) 0 (date-range start-date date))))))

(defn project-to-html [project-code include-chapters]
  (let [stories-to-chapters (reduce (fn [result, story] (assoc result story (db/chapters (:story_key story))))
                                    {}
                                    (db/stories project-code))
        all-chapters (reduce (fn [chapters, story-to-chapter] (into chapters (val story-to-chapter))) [] stories-to-chapters)]
    (do
      (html [:div (burn-up-chart (generate-burn-up-chart-data-points (date 1 10 2011) (date 9 11 2011) all-chapters)
                               (count (date-range (date 1 10 2011) (date 9 11 2011)))
                               (reduce + 0 (map (fn [chapter] (read-string (:size chapter))) all-chapters)))]
          [:div
           [:table {:class "ricardo-table"}
            [:thead [:th "Story Name"] [:th "Size"] [:th "% Done"] [:th "% Dev Budget Used"] [:th "Budget Alert"]]
            (for [story (keys stories-to-chapters)]
              (let [chapters (get stories-to-chapters story)
                    percent-done (calculate-percent-done chapters)
                    percent-budget-used (calculate-percent-budget-used story chapters)]
                (list
                 [:tr
                  [:td (link-to "#" (:story_name story))]
                  [:td (:size story)]
                  [:td (format "%.2f" (float percent-done))]
                  [:td (format "%.2f" (float percent-budget-used))]
                  [:td (budget-alert percent-budget-used percent-done 85)]]
                 (if include-chapters
                   [:tr 
                    [:td {:colspan "5"}
                     [:table {:width "100%"}
                      [:thead [:th "Chapter Name"] [:th "Done"] [:th "Size"] [:th "Budget Used (Days)"]]
                      (for [chapter chapters]
                        [:tr
                         [:td (:chapter_name chapter)]
			 [:td (chapter-done chapter)]
                         [:td (:size chapter)]
                         [:td (format "%.2f" (float (/ (budget-used chapter) 8)))]])]]]))))]]))))

(defn -main [& args]
  (with-command-line
    args
    "Usage: automato [-c] project-code"
    [[include-chapters? c? "include information about chapters in the report ?" false]
     project-code]
    (println (project-to-html (first project-code) include-chapters?))))

