(defproject clip-quiz-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [lynxeyes/dotenv "1.0.2"]
                 [com.stuartsierra/component "1.0.0"]
                 [seancorfield/next.jdbc "1.0.424"]
                 [mysql/mysql-connector-java "8.0.19"]
                 [clj-liquibase "0.6.0"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler clip-quiz-api.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
