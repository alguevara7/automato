(ns automato.db
  (:use [clojure.core]
        [clojure.contrib.properties]
        [clojure.java.io])
  (:require [clojure.string :as str]
            [clojure.java.jdbc :as sql]
            [clojure.contrib.seq :as seq1])
  (:import (java.net URI)
           (java.util Properties)))

(defn- property-value [name]
  (.getProperty (read-properties (str (System/getenv "HOME") "/.automato")) name))

(defn database-resource []
  (let [url (URI. (str "jdbc://" (property-value "hostname") ":1433/unanet"))
        host (.getHost url)
        port (if (pos? (.getPort url)) (.getPort url) 1433)
        path (.getPath url)]
    (merge
     {:subname (str "//" host ":" port path)})))

(def db
  (merge
   {:classname "net.sourceforge.jtds.jdbc.Driver"
    :subprotocol "jtds:sqlserver"
    :user (property-value "user")
    :password (property-value "password")}
   (database-resource)))

(defn project [project-code]
  (sql/with-connection db
    (sql/with-query-results results
      [(str "SELECT p.project_key project_key, p.start_time start_time, p.end_time end_time "
                    "FROM project p "
                    "WHERE p.project_code = ? ") project-code]
      (into [] results))))

(defn stories [project-code]
  (sql/with-connection db
    (sql/with-query-results results
      [(str "SELECT t.task_key story_key, t.task_name story_name, t.user01 size, t.user05 status "
                    "FROM project p, task t "
                    "WHERE t.project_key = p.project_key "
                    "AND p.project_type_key=3 AND p.project_status_key=1 "
                    "AND p.project_code = ? "
                    "AND t.parent_task IS NULL "
                    "AND t.task_name LIKE '%%Story%%' "
                    "ORDER BY t.sequence ") project-code]
      (into [] results))))

(defn chapters [story-key]
  (sql/with-connection db
    (sql/with-query-results results
      [(str "SELECT t.task_key chapter_key, t.task_name chapter_name, t.user01 size, t.user05 status, t.completed, ptd.budget_used "
            "FROM task t "
            "LEFT JOIN (SELECT task_key, SUM(quantity) budget_used FROM person_time_data GROUP BY task_key) ptd ON ptd.task_key = t.task_key "
            "WHERE t.parent_task = ? "
            "AND (t.task_name LIKE '%%CHAPTER%%' OR t.task_name LIKE '%%CHPT%%' OR t.task_name LIKE '%DAT%' OR t.task_name LIKE '%BCP/QA%') "
            "ORDER BY t.sequence ") story-key]
      (into [] results))))


