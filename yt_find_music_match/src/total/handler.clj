(ns total.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [total.routes.home :refer [home-routes]]
            [noir.session :as session]
            [ring.middleware.session.memory :refer [memory-store]]))

(defn init []
  (println "total is starting"))

(defn destroy []
  (println "total is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (->
    (handler/site
      (routes 
        home-routes))
    (session/wrap-noir-session
      {:store (memory-store) })))