(ns excimer.nrepl.server
  (:require [clojure.tools.nrepl.server :refer [start-server stop-server]]))

(defonce ^{:doc "A global nREPL server instance."} nrepl-server (atom nil))

(defn start-nrepl-server
  ([] (start-nrepl-server 0))
  ([port]
   (when-some [s @nrepl-server]
     (stop-server s))
   (reset! nrepl-server (start-server :port port))))

(defn stop-nrepl-server
  []
  (when-some [s @nrepl-server]
    (do
      (stop-server s)
      (reset! nrepl-server nil))))

(defn get-port
  "Get the port which the nREPL server is listening on."
  []
  (when-some [s @nrepl-server]
    (:port s)))

(defn is-running
  []
  (if-some [s @nrepl-server]
    true
    false))
