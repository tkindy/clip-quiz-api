(ns clip-quiz-api.db
  (:require [com.stuartsierra.component :as component]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [dotenv :refer [env]]
            [lambdaisland.uri :refer [uri]]
            [clojure.string :refer [replace]]))

(defrecord DB [ds]
  component/Lifecycle
  (start [this]
    (let [db-uri (uri (env :DATABASE_URL))
          db-spec {:dbtype "postgresql" :dbname (replace (:path db-uri) "/" "")
                   :host (:host db-uri) :port (:port db-uri)
                   :user (:user db-uri) :password (:password db-uri)
                   :sslmode "require"}]

      (assoc this :ds (jdbc/get-datasource db-spec))))

  (stop [this] this))

(defn db []
  (map->DB {}))


;; Queries

(defn test-query [db]
  (sql/query (:ds db) ["SELECT * FROM information_schema.tables"]))
