(ns clip-quiz-api.routes.players
  (:require [compojure.core :refer [routes POST]]
            [ring.util.response :refer [response]]
            [clip-quiz-api.db :refer [insert-player]]))

(def max-name-len 16)

(defn check-name [name]
  (cond
    (> (count name) max-name-len) "Name too long"
    :else nil))

(defn make-player-routes [db]
  (routes
   (POST "/" {{:keys [name]} :body}
     (let [name-error (check-name name)]
       (if name-error
         (-> (response {:ok false :error name-error})
             (assoc :status 400))
         (let [player (insert-player db name)]
             (-> (response nil)
                 (assoc :session {:player-id (:players/id player)}))))))))
