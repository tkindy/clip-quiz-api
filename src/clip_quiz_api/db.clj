(ns clip-quiz-api.db
  (:require [com.stuartsierra.component :as component]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [dotenv :refer [env]]
            [clojure.string :refer [replace-first]]))

(defrecord DB [ds]
  component/Lifecycle
  (start [this]
    (let [db-url (as-> (env :DATABASE_URL) url
                   (replace-first url "postgres://" "postgresql://")
                   (str "jdbc:" url))]
      (assoc this :ds (jdbc/get-datasource db-url))))

  (stop [this] this))

(defn db []
  (map->DB {}))


;; Queries

(defn test-query [db]
  (sql/query (:ds db) ["SELECT * FROM information_schema.tables"]))
