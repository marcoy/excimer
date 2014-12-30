(ns excimer.class-loading
  (:import [java.net URL URLClassLoader]
           [java.io File]))

(def codec-jar "/Users/myuen/.m2/repository/commons-codec/commons-codec/1.9/commons-codec-1.9.jar")
(def chain-jar "/Users/myuen/.m2/repository/commons-chain/commons-chain/1.2/commons-chain-1.2.jar")

(defn to-url [^String str-path]
  (let [jar-file (File. str-path)]
    (when (.exists jar-file)
      (.. jar-file toURI toURL))))

(defn get-class-loader [& paths]
  (let [urls (into-array URL (filter some? (map to-url paths)))]
    urls))
