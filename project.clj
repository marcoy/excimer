(defproject excimer "0.1.0-SNAPSHOT"
  :description "Clojure RPEL and btrace"

  :url "http://marcoyuen.com"

  :license {:name "GNU General Public License, version 3"
            :url "http://www.gnu.org/copyleft/gpl.html"}

  :min-lein-version "2.0.0"

  :repl-options { :init-ns excimer.repl }

  :plugins [[lein-cprint "1.0.0"]]

  :source-paths ["src/main/clj"]

  :java-source-paths ["src/main/java"]

  :dependencies [[aprint "0.1.1"]
                 [commons-io/commons-io "2.4"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/tools.namespace "0.2.7"]
                 [org.clojure/tools.nrepl "0.2.6"]
                 [slingshot "0.12.1"]]
  :profiles {
             :provided
             {
              :dependencies [[org.springframework/spring-context "4.1.3.RELEASE"]]
             }
            })
