(ns clip-quiz-api.handler
  (:require [compojure.core :refer [defroutes context GET]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [clip-quiz-api.app :refer [get-tables]]
            [clip-quiz-api.routes.spotify :refer [spotify-routes]]
            [org.httpkit.server :refer [with-channel on-close on-receive send!]]
            [com.stuartsierra.component :as component]))

(def clients (atom {}))

(defn ws [req]
  (with-channel req conn
    (swap! clients assoc conn true)
    (println conn "connected")
    (on-receive conn (fn [msg] (println conn ":" msg)))
    (on-close conn (fn [status]
                     (swap! clients dissoc conn)
                     (println conn "disconnected. status:" status)))))

(defroutes app-routes
  (GET "/" {app ::app} {:body (map :tables/table_name (get-tables app))})
  (GET "/obj" [] {:body {:name "Tyler" :age 24 :hungry true :aliases ["Gene" "Ted"]}})
  (GET "/push" [] ws)
  (context "/spotify" [] spotify-routes)
  (route/not-found "Not Found"))

(defn wrap-app-component [f app]
  (fn [req]
    (f (assoc req ::app app))))

(defrecord Handler [app handler]
  component/Lifecycle
  (start [this]
    (future (loop [i 0]
              (doseq [client @clients]
                (send! (key client) (str i) false))
              (Thread/sleep 5000)
              (recur (+ i 1))))

    (assoc this :handler
           (-> app-routes
               (wrap-app-component app)
               wrap-json-response
               wrap-cookies
               (wrap-defaults site-defaults))))
  (stop [this]
    (assoc this :handler nil)))

(defn handler []
  (component/using (map->Handler {})
                   [:app]))

(defn get-handler [handler]
  (:handler handler))
