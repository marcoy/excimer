(ns excimer.btrace.connection
  (:require [excimer.btrace.commands :refer [send-exit]])
  (:import [java.io PrintWriter ObjectInputStream ObjectOutputStream]
           [java.net Socket]))

(defonce btrace-connection (atom nil))

(defn close-connection
  [s]
  (let [{:keys [sock ois oos writer]} s]
    (do
      (send-exit oos)
      (reset! btrace-connection nil))))

(defn new-connection
  ([agent-port] (new-connection agent-port *out*))
  ([agent-port ^PrintWriter writer]
   (do
     (when-first [s @btrace-connection]
       (close-connection s))
     (let [sock (Socket. "127.0.0.1" agent-port)
           oos  (ObjectOutputStream. (.getOutputStream sock))
           ois  (ObjectInputStream. (.getInputStream sock))]
       (reset! btrace-connection {:sock sock :oos oos :ois ois :writer nil})))))
