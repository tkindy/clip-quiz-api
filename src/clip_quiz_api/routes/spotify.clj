(ns clip-quiz-api.routes.spotify
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [ring.util.response :refer [redirect response set-cookie]]
            [clip-quiz-api.util :refer [build-url clear-cookie encode-base64]]
            [clj-http.client :as http]
            [dotenv :refer [env]]))

(def state-len 16)
(def state-alphabet "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789")

(defn gen-state []
  (let [chars (map (fn [_] (rand-nth state-alphabet))
                   (range state-len))]
    (apply str chars)))

(def required-scopes "user-modify-playback-state user-read-playback-state")
(def state-key "spotify_auth_state")
(def access-token-key "spotify_access_token")
(def refresh-token-key "spotify_refresh_token")

(def client-id (env :SPOTIFY_CLIENT_ID))
(def client-secret (env :SPOTIFY_CLIENT_SECRET))
(def redirect-uri (env :SPOTIFY_AUTH_REDIRECT_URI))
(def redirect-query-base {:response_type "code" :client_id client-id
                          :scope required-scopes :redirect_uri redirect-uri})

(defn bad-state? [state req]
  (let [stored-state (get-in req [:cookies state-key])]
    (or (nil? state) (not (= state stored-state)))))

(def auth-options-base {:form-params {:redirect_uri redirect-uri :grant_type "authorization_code"}
                        :headers {"Authorization" (str "Basic " (encode-base64 (str client-id ":" client-secret)))}
                        :accept :json
                        :content-type :json})

(defroutes spotify-routes
  (GET "/login" []
    (let [state (gen-state)
          redirect-query (assoc redirect-query-base :state state)]
      (println "State:" state)
      (-> (build-url "https://accounts.spotify.com/authorize" redirect-query)
          redirect
          (set-cookie state-key state))))

  (GET "/callback" [code state :as req]
    (if (bad-state? state req)
      (assoc (response "Invalid state") :status 400)
      (let [auth-options (assoc-in auth-options-base [:form-params :code] code)
            {{access-token :access_token refresh-token :refresh_token} :body }
            (http/post "https://accounts.spotify.com/api/token" auth-options)]
        (-> (response nil)
            (set-cookie access-token-key access-token)
            (set-cookie refresh-token-key refresh-token)
            (clear-cookie state-key)))))

  (route/not-found "Not Found"))
