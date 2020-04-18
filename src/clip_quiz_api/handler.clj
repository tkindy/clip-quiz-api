(ns clip-quiz-api.handler
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response]]
            [clip-quiz-api.app :refer [get-tables]]
            [org.httpkit.server :refer [with-channel on-close on-receive send!]]))

(def clients (atom {}))

(defn ws [req]
  (with-channel req conn
    (swap! clients assoc conn true)
    (println conn "connected")
    (on-receive conn (fn [msg] (println conn ":" msg)))
    (on-close conn (fn [status]
                     (swap! clients dissoc conn)
                     (println conn "disconnected. status:" status)))))

(defroutes routes
  (GET "/" {app ::app} {:body (map :tables/table_name (get-tables app))})
  (GET "/obj" [] {:body {:name "Tyler" :age 24 :hungry true :aliases ["Gene" "Ted"]}})
  (GET "/push" [] ws)
  (route/not-found "Not Found"))

(defn wrap-app-component [f app]
  (fn [req]
    (f (assoc req ::app app))))

(defn make-handler [app]
  (future (loop [i 0]
            (doseq [client @clients]
              (send! (key client) (str i) false))
            (Thread/sleep 5000)
            (recur (+ i 1))))

  (-> routes
      (wrap-app-component app)
      (wrap-json-response)
      (wrap-defaults site-defaults)))
