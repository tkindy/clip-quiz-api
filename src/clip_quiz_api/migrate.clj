(ns clip-quiz-api.migrate
  (:require [clj-liquibase.cli :as cli]
            [clj-liquibase.core :refer [defparser]]
            [next.jdbc :as jdbc]))

(defparser app-changelog "changelog.sql")

(def ds (jdbc/get-datasource
         {:dbtype "mysql" :host "172.17.0.1" :dbname "clip-quiz"
          :user "root" :password "mysecretpw"}))

(defn -main [& [cmd & args]]
  (apply cli/entry cmd {:datasource ds :changelog app-changelog}
         args))
