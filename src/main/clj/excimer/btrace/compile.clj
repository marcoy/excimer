(ns excimer.btrace.compile
  (:require [clojure.tools.logging :as log]
            [excimer.paths :refer [jar-path append-classpath]]
            [excimer.class-loading :refer [load-class param-array]])
  (:import [java.io File]
           [org.apache.commons.io FileUtils FilenameUtils]))

(defonce clientjar-path (jar-path "btrace-client.jar"))

(defn create-btrace-client
  "Create an instance of `com.sun.btrace.client.Client`."
  ([] (create-btrace-client 0))
  ([btrace-port] (create-btrace-client btrace-port false false))
  ([btrace-port debug unsafe] (let [client-class (load-class "com.sun.btrace.client.Client" clientjar-path)
                                    client-inst (-> (.getConstructor client-class (into-array Class
                                                                                              [Integer/TYPE (class ".")
                                                                                               Boolean/TYPE Boolean/TYPE
                                                                                               Boolean/TYPE Boolean/TYPE
                                                                                               (class ".")]))
                                                    (.newInstance (into-array Object [(int btrace-port) "."
                                                                                      (boolean debug) false
                                                                                      (boolean unsafe) false nil])))]
                                client-inst)))

(defn btrace-compile
  "Compile the given java file, and return the bytecode as a byte array."
  [btrace-client java-file]
  (let [cp (append-classpath clientjar-path)]
    (log/info "Compiling" java-file "with classpath" cp)
    (.compile btrace-client java-file cp)))

(defn btrace-compile-to-file
  "Compile the given java file, and write the bytecode to a file."
  ([btrace-client java-file ^File out-file]
    (let [bytecode (btrace-compile btrace-client java-file)]
      (FileUtils/writeByteArrayToFile out-file bytecode)))
  ([btrace-client java-file]
    (let [out-file (-> java-file
                      FilenameUtils/getBaseName
                      (str ".class"))]
      (btrace-compile-to-file btrace-client java-file (File. out-file)))))
