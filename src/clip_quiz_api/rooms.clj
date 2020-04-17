(ns clip-quiz-api.rooms
  (:require [com.stuartsierra.component :as component]))

(defrecord Rooms [db]
  component/Lifecycle)

(defn rooms []
  (component/using (map->Rooms {})
                   [:db]))
