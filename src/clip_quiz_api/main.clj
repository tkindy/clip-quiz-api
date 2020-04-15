(ns clip-quiz-api.main
  (:require [com.stuartsierra.component :as component]
            [clip-quiz-api.system :as system]
            [clojure.tools.logging :as log]))

(def system)

(defn stop []
  (log/info "Stopping system...")
  (alter-var-root #'system component/stop-system)
  (log/info "Stopped system"))

(.addShutdownHook (Runtime/getRuntime) (Thread. ^Runnable shutdown-agents))
(.addShutdownHook (Runtime/getRuntime) (Thread. ^Runnable stop))

(defn -main []
  (let [start (comp component/start-system (fn [_] (system/system)))]
    (alter-var-root #'system start)))
