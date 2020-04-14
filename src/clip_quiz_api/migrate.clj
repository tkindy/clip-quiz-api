(ns clip-quiz-api.migrate
  (:require [clj-liquibase.cli :as lqb]
            [clj-liquibase.core :refer [parse-changelog]]
            [next.jdbc :as jdbc]
            [dotenv :refer [env]]))

(defn -main [& [cmd & args]]
  (let [ds        (jdbc/get-datasource (env :MIGRATION_DATABASE_URL))
        changelog (partial parse-changelog "changelog.sql")
        opts      {:datasource ds :changelog changelog}]
    (apply lqb/entry cmd opts args)))
