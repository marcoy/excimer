(defproject excimer "0.2.1-SNAPSHOT"
  :description "Clojure RPEL and btrace"

  :url "http://marcoyuen.com"

  :license {:name "GNU General Public License, version 3"
            :url "http://www.gnu.org/copyleft/gpl.html"}

  :min-lein-version "2.0.0"

  :javac-options ["-target" "1.7" "-source" "1.7"]

  :repl-options { :init-ns excimer.repl }

  :plugins [[codox "0.8.12"]
            [lein-cprint "1.1.0"]]

  :codox {:defaults {:doc/format :markdown}}

  :source-paths ["src/main/clj"]

  :java-source-paths ["src/main/java"]

  :dependencies [[aprint "0.1.3"]
                 [commons-io/commons-io "2.4"]
                 [org.clojure/clojure "1.7.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.clojure/tools.namespace "0.2.10"]
                 [org.clojure/tools.nrepl "0.2.10"]
                 [org.slf4j/slf4j-api "1.7.12"]
                 [slingshot "0.12.2"]]

  :profiles {
             :provided
             {
              :dependencies [[org.springframework/spring-context "4.2.0.RELEASE"]]
             }
            })
