(ns clip-quiz-api.db
  (:require [com.stuartsierra.component :as component]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [dotenv :refer [env]]))

(defrecord DB [ds]
  component/Lifecycle
  (start [this]
    (assoc this
           :ds (jdbc/get-datasource (env :DATABASE_URL))))

  (stop [this] this))

(defn db []
  (map->DB {}))


;; Queries

(defn test-query [db]
  (sql/query (:ds db) ["SELECT * FROM information_schema.tables"]))
