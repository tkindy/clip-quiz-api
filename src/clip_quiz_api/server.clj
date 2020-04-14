(ns clip-quiz-api.server
  (:require [ring.server.standalone :refer [serve]]
            [clip-quiz-api.handler :refer [make-handler]]
            [com.stuartsierra.component :as component]
            [dotenv :refer [env]]
            [clojure.tools.logging :as log]))

(defrecord WebServer [app server]
  component/Lifecycle
  (start [this]
    (log/info "starting webserver")
    (let [config {:port (Integer/parseInt (env :PORT))
                  :open-browser? false}]
      (assoc this
             :server (serve (make-handler app) config))))
  (stop [this]
    (log/info "stopping webserver")
    (.stop server)
    (assoc this :server nil)))

(defn web-server []
  (component/using (map->WebServer {})
                   [:app]))
