(ns clip-quiz-api.server
  (:require [org.httpkit.server :refer [run-server]]
            [clip-quiz-api.handler :refer [get-handler]]
            [com.stuartsierra.component :as component]
            [clojure.tools.logging :as log]))

(defrecord WebServer [port handler stop-server]
  component/Lifecycle
  (start [this]
    (log/info "starting webserver")
    (let [config {:port port}]
      (assoc this
             :stop-server (run-server (get-handler handler) config))))
  (stop [this]
    (log/info "stopping webserver")
    (stop-server)
    (assoc this :stop-server nil)))

(defn web-server [& {:keys [port]}]
  (assert port)
  (component/using (map->WebServer {:port port})
                   [:handler]))
