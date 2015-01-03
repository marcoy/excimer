(ns excimer.btrace.compile
  (:require [clojure.tools.logging :as log]
            [excimer.paths :refer [jar-path append-classpath]]
            [excimer.class-loading :refer [load-class param-array]])
  (:import [java.io File]
           [org.apache.commons.io FileUtils FilenameUtils]))

(defonce clientjar-path (jar-path "btrace-client.jar"))

(defn create-btrace-client
  "Create an instance of com.sun.btrace.client.Client."
  ([] (create-btrace-client 0))
  ([btrace-port] (let [client-class (load-class "com.sun.btrace.client.Client" clientjar-path)
                       client-inst (-> (.getConstructor client-class (into-array Class [Integer/TYPE]))
                                       (.newInstance (into-array [(int btrace-port)])))]
                   client-inst)))

(defn btrace-compile
  [btrace-client java-file]
  (let [cp (append-classpath clientjar-path)]
    (log/info "Compiling" java-file "with classpath" cp)
    (.compile btrace-client java-file cp)))

(defn btrace-compile-to-file
  ([btrace-client java-file ^File out-file]
    (let [bytecode (btrace-compile btrace-client java-file)]
      (FileUtils/writeByteArrayToFile out-file bytecode)))
  ([btrace-client java-file]
    (let [out-file (-> java-file
                      FilenameUtils/getBaseName
                      (str ".class"))]
      (btrace-compile-to-file btrace-client java-file (File. out-file)))))
