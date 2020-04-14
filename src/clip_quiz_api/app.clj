(ns clip-quiz-api.app
  (:require [com.stuartsierra.component :as component]
            [clojure.tools.logging :as log]))

(defrecord App []
  component/Lifecycle
  (start [this]
    (log/info "starting app")
    this)
  (stop [this]
    (log/info "stopping app")
    this))

(defn app []
  (map->App {}))
