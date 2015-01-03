(ns excimer.paths
  (:require [clojure.java.io :as io])
  (:import [org.apache.commons.io FileUtils]
           [java.io File]))

(defonce classpath (System/getProperty "java.class.path"))
(defonce java-home (System/getProperty "java.home"))
(defonce sys-tmp-dir (System/getProperty "java.io.tmpdir"))


(defn- get-toolsjar-classpath
  "Locate tools.jar in the classpath"
  []
  (first (filter #(.endsWith % "tools.jar")
                 (clojure.string/split classpath #":"))))

(defn- get-toolsjar-javahome
  "Locate tools.jar in ${java.home}"
  []
  (let [file (File. (clojure.string/join File/separator
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

(defn jar-path
  "Attempt to locate a jar in the resources and return its path. If the jar is
   inside another jar, it will write out the jar to /tmp."
  [jar-name]
  (when-some [jar-url (io/resource jar-name)]
    (let [jar-file (File. (.getPath jar-url))]
      (if (.exists jar-file)
        (.getAbsolutePath jar-file)
        (let [jar-stream (io/input-stream jar-url)
              tmp-jar-file (File. (clojure.string/join File/separator
                                    ["/tmp" jar-name]))]
          (do (FileUtils/copyInputStreamToFile jar-stream tmp-jar-file)
              (.getAbsolutePath tmp-jar-file)))))))

(defn append-classpath [& jars]
  (let [system-cp (System/getProperty "java.class.path")]
    (clojure.string/join (File/pathSeparator) (concat (list system-cp) jars))))
