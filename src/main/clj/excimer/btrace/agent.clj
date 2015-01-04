(ns excimer.btrace.agent
  (:require [excimer.paths :refer [find-toolsjar jar-path]]
            [excimer.vm :refer [attach-current-jvm]]))


(defonce bootjar-path (jar-path "btrace-boot.jar"))
(defonce agentjar-path (jar-path "btrace-agent.jar"))


(defn load-agent
  "Attach to the current jvm, and load the btrace agent."
  [& {:keys [port debug unsafe systemClassPath bootClassPath trackRetransforms probeDescPath]
      :as arg-params}]
  (let [default-args {:port 3030 :debug true :unsafe true :systemClassPath (find-toolsjar)
                      :bootClassPath bootjar-path :trackRetransforms true :probeDescPath "."}
        merged-args (merge default-args arg-params)
        agent-args (clojure.string/join "," (for [[k v] merged-args] (clojure.string/join "=" [(name k) (str v)])))]
    (let [vm (attach-current-jvm)]
      (.loadAgent vm agentjar-path agent-args))))
