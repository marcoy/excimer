(ns excimer.btrace
  (:require [excimer.btrace.agent :as btagent])
  (:import [java.io File]))

(defonce load-agent btagent/load-agent)
