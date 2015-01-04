(ns excimer.btrace.connection
  (:require [clojure.tools.logging :as log]
            [excimer.btrace.commands :refer [printcmd readbytes send-exit]])
  (:import [java.io EOFException PrintWriter ObjectInputStream ObjectOutputStream]
           [java.net Socket]))

(defonce ^{:doc "A global btrace-agent connection."} btrace-connection (atom nil))

(defn- printer-loop
  [ois print-writer]
  (try
    (loop []
      (let [cmd-type (.readByte ois)
            cmd (readbytes cmd-type ois)]
        (condp contains? (:type cmd)
          #{4 6 9} (printcmd cmd print-writer)
          (do (.println print-writer cmd)
              (.flush print-writer))))
      (recur))
    (catch EOFException e (log/info "Printer loop stopped"))
    (catch InterruptedException e (log/info "Printer loop interrupted"))))

(defn close-connection
  "Close an established btrace connection."
  [c]
  (let [{:keys [sock ois oos printer]} c]
    (do
      (send-exit oos)
      (reset! btrace-connection nil))))

(defn new-connection
  "Establish a connection to the btrace-agent."
  ([agent-port] (new-connection agent-port *out*))
  ([agent-port ^PrintWriter print-writer]
   (do
     (when-some [s @btrace-connection]
       (close-connection s))
     (let [sock (Socket. "127.0.0.1" agent-port)
           oos  (ObjectOutputStream. (.getOutputStream sock))
           ois  (ObjectInputStream. (.getInputStream sock))
           printer-thread (Thread. #(printer-loop ois print-writer))]
       (.start printer-thread)
       (reset! btrace-connection {:sock sock :oos oos :ois ois :printer printer-thread})))))
