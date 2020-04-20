(ns clip-quiz-api.server
  (:require [org.httpkit.server :refer [run-server]]
            [clip-quiz-api.handler :refer [get-handler]]
            [com.stuartsierra.component :as component]
            [clojure.tools.logging :as log]))

(defrecord WebServer [port handler stop-server]
  component/Lifecycle
  (start [this]
    (let [config {:port port}
          stop-server (run-server (get-handler handler) config)]
      (log/info "Started server")
      (assoc this :stop-server stop-server)))
  (stop [this]
    (stop-server)
    (log/info "Stopped server")
    (assoc this :stop-server nil)))

(defn web-server [& {:keys [port]}]
  (assert port)
  (component/using (map->WebServer {:port port})
                   [:handler]))
