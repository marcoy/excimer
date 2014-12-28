(ns excimer.btrace.session
  (:require [excimer.btrace.commands :refer [send-exit]])
  (:import [java.io PrintWriter ObjectInputStream ObjectOutputStream]
           [java.net Socket]))

(defonce nrepl-session (atom nil))

(defn close-session
  [s]
  (let [{:keys [sock ois oos writer]} s]
    (do
      (send-exit oos)
      (reset! nrepl-session nil))))

(defn new-session
  ([agent-port] (new-session agent-port *out*))
  ([agent-port ^PrintWriter writer]
   (do
     (when-first [s @nrepl-session]
       (close-session s))
     (let [sock (Socket. "127.0.0.1" agent-port)
           oos  (ObjectOutputStream. (.getOutputStream sock))
           ois  (ObjectInputStream. (.getInputStream sock))]
       (reset! nrepl-session {:sock sock :oos oos :ois ois :writer nil})))))
