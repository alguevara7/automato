(defproject automato "0.0.1-SNAPSHOT"
  :description "an automation shell"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.clojure/java.jdbc "0.1.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [net.sourceforge.jtds/jtds "1.2.4"]
                 [hiccup "0.3.7"]
                 [clj-cache "0.0.4"]
                 [clj-cache-ehcache "0.0.4"]
                 [org.slf4j/slf4j-simple "1.6.1"]]
  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public"}
  :main automato.core)
