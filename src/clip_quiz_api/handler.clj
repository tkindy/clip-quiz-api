(ns clip-quiz-api.handler
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response]]
            [clip-quiz-api.app :refer [get-tables]]))

(defroutes routes
  (GET "/" {app ::app} {:body (map :TABLES/TABLE_NAME (get-tables app))})
  (GET "/obj" [] {:body {:name "Tyler" :age 24 :hungry true :aliases ["Gene" "Ted"]}})
  (route/not-found "Not Found"))

(defn wrap-app-component [f app]
  (fn [req]
    (f (assoc req ::app app))))

(defn make-handler [app]
  (-> routes
      (wrap-app-component app)
      (wrap-json-response)
      (wrap-defaults site-defaults)))
