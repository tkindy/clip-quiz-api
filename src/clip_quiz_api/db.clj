(ns clip-quiz-api.db
  (:require [com.stuartsierra.component :as component]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [dotenv :refer [env app-env]]
            [lambdaisland.uri :refer [uri]]
            [clojure.string :as s]))

;; Setup

(defn build-datasource [env-key]
  (let [db-uri (uri (env env-key))
        db-spec {:dbtype "postgresql" :dbname (s/replace (:path db-uri) "/" "")
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

(defn quote-ident [ident] (str "\"" ident "\""))
(def query-opts {:table-fn quote-ident :column-fn quote-ident})

(defn test-query [db]
  (sql/query (:ds db) ["SELECT * FROM information_schema.tables"]))

(defn insert-player [db name]
  (sql/insert! (:ds db) :players {:name name} query-opts))

(defn insert-room [db passphrase playlist-id leader-id]
  (sql/insert! (:ds db) :rooms
               {:passphrase passphrase :playlistId playlist-id :leaderId leader-id}
               query-opts))

(defn enter-room [db player-id room-id]
  (jdbc/execute-one! (:ds db) ["
    INSERT INTO
      \"roomPlayers\" (\"playerId\", \"roomId\")
    VALUES
      (?, ?)
    ON CONFLICT (\"playerId\") DO UPDATE
      SET \"roomId\" = ?
  " player-id room-id room-id] query-opts))
