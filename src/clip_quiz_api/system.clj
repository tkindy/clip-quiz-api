(ns clip-quiz-api.system
  (:require [com.stuartsierra.component :as component]
            [clip-quiz-api.app :refer [app]]
            [clip-quiz-api.server :refer [web-server]]
            [clip-quiz-api.db :refer [db]]
            [dotenv :refer [env]]))

(defn system []
  (component/system-map
   :app (app)
   :web-server (web-server)
   :db (db)))
