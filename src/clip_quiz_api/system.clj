(ns clip-quiz-api.system
  (:require [com.stuartsierra.component :as component]
            [clip-quiz-api.app :refer [app]]
            [clip-quiz-api.server :refer [web-server]]
            [clip-quiz-api.db :refer [db]]
            [clip-quiz-api.handler :refer [handler]]
            [dotenv :refer [env]]))

(defn system []
  (component/system-map
   :app (app)
   :handler (handler)
   :web-server (web-server :port (Integer/parseInt (env :PORT)))
   :db (db)))
