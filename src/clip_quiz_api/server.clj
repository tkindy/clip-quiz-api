(ns clip-quiz-api.server
  (:require [org.httpkit.server :refer [run-server]]
            [clip-quiz-api.handler :refer [make-handler]]
            [com.stuartsierra.component :as component]
            [dotenv :refer [env]]
            [clojure.tools.logging :as log]))

(defrecord WebServer [app stop-server]
  component/Lifecycle
  (start [this]
    (log/info "starting webserver")
    (let [config {:port (Integer/parseInt (env :PORT))}]
      (assoc this
             :stop-server (run-server (make-handler app) config))))
  (stop [this]
    (log/info "stopping webserver")
    (stop-server)
    (assoc this :stop-server nil)))

(defn web-server []
  (component/using (map->WebServer {})
                   [:app]))
