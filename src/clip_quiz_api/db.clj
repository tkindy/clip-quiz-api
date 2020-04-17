(ns clip-quiz-api.db
  (:require [com.stuartsierra.component :as component]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [dotenv :refer [env]]
            [clojure.string :refer [replace-first]]))

(defrecord DB [ds]
  component/Lifecycle
  (start [this]
    (let [db-url (replace-first (env :DATABASE_URL) "postgres://" "postgresql://")]
      (assoc this :ds (jdbc/get-datasource db-url))))

  (stop [this] this))

(defn db []
  (map->DB {}))


;; Queries

(defn test-query [db]
  (sql/query (:ds db) ["SELECT * FROM information_schema.tables"]))
