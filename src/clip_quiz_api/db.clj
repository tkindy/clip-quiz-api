(ns clip-quiz-api.db
  (:require [com.stuartsierra.component :as component]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [dotenv :refer [env app-env]]
            [lambdaisland.uri :refer [uri]]
            [clojure.string :refer [replace]]))

;; Setup

(defn build-datasource [env-key]
  (let [db-uri (uri (env env-key))
        db-spec {:dbtype "postgresql" :dbname (replace (:path db-uri) "/" "")
                 :host (:host db-uri) :port (:port db-uri)
                 :user (:user db-uri) :password (:password db-uri)}
        db-spec (if (= app-env "local") db-spec (assoc db-spec :sslmode "require"))]
    (jdbc/get-datasource db-spec)))

(defrecord DB [ds]
  component/Lifecycle
  (start [this]
    (assoc this :ds (build-datasource :DATABASE_URL)))

  (stop [this] this))

(defn db []
  (map->DB {}))


;; Queries

(defn test-query [db]
  (sql/query (:ds db) ["SELECT * FROM information_schema.tables"]))
