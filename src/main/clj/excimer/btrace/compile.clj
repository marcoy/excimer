(ns excimer.btrace.compile
  (:require [excimer.paths :refer [jar-path]]
            [excimer.class-loading :refer [load-class param-array]]))

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
  (.compile btrace-client java-file (System/getProperty "java.class.path")))
