(ns clip-quiz-api.migrate
  (:require [clj-liquibase.cli :as cli]
            [clj-liquibase.core :refer [defparser]]
            [next.jdbc :as jdbc]
            [dotenv :refer [env]]))

(defparser app-changelog "changelog.sql")

(def ds (jdbc/get-datasource (env :MIGRATION_DATABASE_URL)))

(defn -main [& [cmd & args]]
  (apply cli/entry cmd {:datasource ds :changelog app-changelog}
         args))
