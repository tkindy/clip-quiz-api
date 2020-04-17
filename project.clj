(defproject clip-quiz-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring-server "0.5.0"]
                 [ring/ring-json "0.5.0"]
                 [lynxeyes/dotenv "1.0.2"]
                 [com.stuartsierra/component "1.0.0"]
                 [seancorfield/next.jdbc "1.0.424"]
                 [org.postgresql/postgresql "42.2.11"]
                 [clj-liquibase "0.6.0"]

                 ;; Logging
                 [org.clojure/tools.logging "1.0.0"]
                 [org.slf4j/slf4j-api "1.7.30"]
                 [ch.qos.logback/logback-core "1.2.3"]
                 [ch.qos.logback/logback-classic "1.2.3"]]
  :main clip-quiz-api.main
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}}
  :jvm-opts ["-Dclojure.tools.logging.factory=clojure.tools.logging.impl/slf4j-factory"])
