(ns clip-quiz-api.routes.spotify
  (:require [compojure.core :refer [defroutes routes context GET POST]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [redirect set-cookie]]
            [ring.util.codec :refer [url-encode]]
            [dotenv :refer [env]]))

(def state-len 16)
(def state-alphabet "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789")

(defn gen-state []
  (let [chars (map (fn [_] (rand-nth state-alphabet))
                   (range state-len))]
    (apply str chars)))

;; https://stackoverflow.com/a/3644219
(defn make-query-string [m & [encoding]]
  (let [s #(if (instance? clojure.lang.Named %) (name %) %)
        enc (or encoding "UTF-8")]
    (->> (for [[k v] m]
           (str (url-encode (s k) enc)
                "="
                (url-encode (str v) enc)))
         (interpose "&")
         (apply str))))

(defn build-url [url-base query-map & [encoding]]
  (str url-base "?" (make-query-string query-map encoding)))

(def required-scopes "user-modify-playback-state user-read-playback-state ")
(def state-key "spotify_auth_state")
(def redirect-query-base {:respone_type "code" :client_id (env :SPOTIFY_CLIENT_ID)
                          :scope required-scopes :redirect_uri (env :SPOTIFY_AUTH_REDIRECT_URI)})

(defroutes spotify-routes
  (POST "/login" []
    (let [state (gen-state)
          redirect-query (assoc redirect-query-base :state state)
          redirect-url (build-url "https://accounts.spotify.com/authorize" redirect-query)]
      (set-cookie (redirect redirect-url) state-key state)))
  (route/not-found "Not Found"))
