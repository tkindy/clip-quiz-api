(ns clip-quiz-api.db
  (:require [com.stuartsierra.component :as component]
            [next.jdbc :as jdbc]
            [dotenv :refer [env]]))

(defrecord DB [conn]
  component/Lifecycle
  (start [this]
    (assoc this
           :conn (jdbc/get-datasource (env :DATABASE_URL))))

  (stop [this]
    (.stop conn)
    this))

(defn db []
  (map->DB {}))
