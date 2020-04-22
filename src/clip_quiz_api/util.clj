(ns clip-quiz-api.util
  (:require [ring.util.codec :refer [url-encode]]
            [ring.util.response :refer [set-cookie]])
  (:import java.util.Base64))

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

;; https://stackoverflow.com/a/3644219
(defn build-url [url-base query-map & [encoding]]
  (str url-base "?" (make-query-string query-map encoding)))

(defn clear-cookie [res key]
  (set-cookie res key nil {:max-age 0}))

;; Base64
(defn encode-base64 [s]
  (.encodeToString (Base64/getEncoder) (.getBytes s)))

(defn build-random-string [length alphabet]
  (let [chars (map (fn [_] (rand-nth alphabet))
                   (range length))]
    (apply str chars)))
