(defproject automato "0.0.1-SNAPSHOT"
  :description "an automation shell"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/java.jdbc "0.1.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [net.sourceforge.jtds/jtds "1.2.4"]
                 [hiccup "0.3.7"]]
  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"
                 "maven central" "http://repo1.maven.org/maven2/"}
  :main automato.core)
