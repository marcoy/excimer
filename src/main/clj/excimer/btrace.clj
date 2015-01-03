(ns excimer.btrace
  (:require [clojure.java.io :as io]
            [excimer.btrace.agent :as btagent]
            [excimer.btrace.connection :as btconn]
            [excimer.btrace.commands :as btcom])
  (:import [java.io File]
           [org.apache.commons.io IOUtils]))

(defonce load-agent btagent/load-agent)
(defonce new-btrace-connection btconn/new-connection)
(defonce close-btrace-connection btconn/close-connection)

(defn recv-btrace-messages
  [ois]
  (loop []
    (let [cmd-type (.readByte ois)
          cmd (btcom/readbytes cmd-type ois)]
      (condp contains? (:type cmd)
        #{4 6 9} (btcom/printcmd cmd *out*)
        (println cmd)))
    (recur)) )

(defn instrument-jvm
  ([class-file-path]
    (if-some [conn (deref btconn/btrace-connection)]
      (instrument-jvm conn class-file-path)
      (println "Need to create a new connection")))
  ([conn class-file-path]
    (let [class-file (File. class-file-path)]
      (if (.exists class-file)
        (let [{:keys [sock ois oos]} conn
              bytecode (IOUtils/toByteArray (io/input-stream class-file-path))
              ic (btcom/instrument-command bytecode [])]
          (btcom/writebytes ic oos)
          (recv-btrace-messages ois))
        (println class-file-path "doesn't exist or a wrong path is given")))))
