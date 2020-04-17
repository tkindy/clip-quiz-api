(ns clip-quiz-api.migrate
  (:require [clj-liquibase.cli :as lqb]
            [clj-liquibase.core :refer [parse-changelog]]
            [clip-quiz-api.db :refer [build-datasource]]))

(defn -main [& [cmd & args]]
  (let [ds        (build-datasource :DATABASE_URL)
        changelog (partial parse-changelog "changelog.sql")
        opts      {:datasource ds :changelog changelog}]
    (apply lqb/entry cmd opts args)))
