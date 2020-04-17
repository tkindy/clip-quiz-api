(ns clip-quiz-api.app
  (:require [com.stuartsierra.component :as component]
            [clip-quiz-api.db :refer [test-query]]
            [clojure.tools.logging :as log]))

(defrecord App [db]
  component/Lifecycle
  (start [this] this)
  (stop [this] this))

(defn app []
  (component/using (map->App {})
                   [:db]))


;; Functions

(defn get-tables [app]
  (test-query (:db app)))
