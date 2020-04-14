(ns clip-quiz-api.handler
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes routes
  (GET "/" [] "Hello World")
  (route/not-found "Not Found"))

(defn wrap-app-component [f app]
  (fn [req]
    (f (assoc req ::app app))))

(defn make-handler [app]
  (-> routes
      (wrap-app-component app)
      (wrap-defaults site-defaults)))
