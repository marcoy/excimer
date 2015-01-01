(ns excimer.vm
  (:require [excimer.paths :refer [find-toolsjar]]
            [excimer.class-loading :refer [load-class invoke-static-method]])
  (:import [java.lang.management ManagementFactory]))


(defn ^String jvm-pid
  "Get the PID of the current JVM. Not portable. Use with caution."
  []
  (-> (.. ManagementFactory getRuntimeMXBean getName)
      (clojure.string/split #"@")
      first))

(defn load-vm-class []
  (let [toolsjar-path (find-toolsjar)]
    (load-class "com.sun.tools.attach.VirtualMachine" toolsjar-path)))

(defn attach-jvm [^String pid]
  (let [vm (load-vm-class)]
    (invoke-static-method vm "attach" pid)))

(defn attach-current-jvm []
  (attach-jvm (jvm-pid)))
