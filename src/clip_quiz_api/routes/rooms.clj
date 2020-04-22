(ns clip-quiz-api.routes.rooms
  (:require [compojure.core :refer [routes POST]]
            [ring.util.response :refer [response]]
            [clip-quiz-api.util :refer [build-random-string]]
            [clip-quiz-api.db :refer [insert-room enter-room]]))

(def passphrase-length 4)
(def passphrase-alphabet "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789")

(defn check-create-room-req [player-id playlist-id]
  (cond
    (not player-id) "Missing player ID"
    (not playlist-id) "Missing playlist ID"
    :else nil))

(defn create-room [db player-id playlist-id]
  (let [req-error (check-create-room-req player-id playlist-id)]
    (if req-error
      (-> (response {:ok false :error req-error})
          (assoc :status 400))
      (let [passphrase (build-random-string passphrase-length passphrase-alphabet)
            room (insert-room db passphrase playlist-id player-id)]
        (enter-room db player-id (:rooms/id room))
        (response nil)))))

(defn make-room-routes [db]
  (routes
   (POST "/" {{:keys [player-id]} :session
              {playlist-id :playlistId} :body}
     (create-room db player-id playlist-id))))
