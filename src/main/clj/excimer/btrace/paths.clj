(ns excimer.btrace.paths
  (:require [clojure.java.io :as io])
  (:import [java.io File]))

(defonce classpath (System/getProperty "java.class.path"))
(defonce java-home (System/getProperty "java.home"))


(defn- jar-path
  "Attempt to locate a jar in the resources and return its path."
  [jar-name]
  (.getPath (io/resource jar-name)))

(defn- get-toolsjar-classpath
  "Locate tools.jar in the classpath"
  []
  (first (filter #(.endsWith % "tools.jar")
                 (clojure.string/split classpath #":"))))

(defn- get-toolsjar-javahome
  "Locate tools.jar in ${java.home}"
  []
  (let [file (File. (clojure.string/join java.io.File/separator
                      [(System/getProperty "java.home") ".." "lib" "tools.jar"]))]
    (if (.exists file)
      (.getPath file)
      nil)))

(defn- get-toolsjar-guess
  "Out of option. Guess the location of tools.jar (INCOMPLETE)"
  []
  (let [os-name (System/getProperty "os.name")]
    "/usr/java/latest/lib/tools.jar"))

(defn find-toolsjar
  "Try to locate tools.jar from various locations and return a path to it."
  []
  (let [toolsjar-classpath (get-toolsjar-classpath)
        toolsjar-javahome  (get-toolsjar-javahome)
        toolsjar-guess     (get-toolsjar-guess)]
    (some #(when-some [toolsjar %] toolsjar)
          [toolsjar-classpath toolsjar-javahome toolsjar-guess])))

