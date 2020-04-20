(ns clip-quiz-api.main
  (:require [com.stuartsierra.component :as component]
            [clip-quiz-api.system :as system]))

(def system)

(defn stop []
  (alter-var-root #'system component/stop-system))

(defn -main []
  (.addShutdownHook (Runtime/getRuntime) (Thread. ^Runnable shutdown-agents))
  (.addShutdownHook (Runtime/getRuntime) (Thread. ^Runnable stop))
  (let [started (comp component/start-system (fn [_] (system/system)))]
    (alter-var-root #'system started)))
