(ns total.routes.home
  (:require [compojure.core :refer :all]
            [total.views.layout :as layout]
            [hiccup.form :refer :all]
            [noir.session :as session]
            [clojure.java.io :as io]
            [total.models.db :as db]
            [total.file-content-manipulation :as content]
            [total.algorithm :as algorithm])
  (:use clojure.java.io
        clojure.java.browse))

(defn String->Number [str]
  "Converts string to number"
     (let [n (read-string str)]
          (if (number? n) n nil)))

(defn welcome []
  "Welcome page"
  (layout/common [:h1 "Welcome to YouTube random jukebox!"]
     (form-to [:post "/register"]
               (submit-button "Register"))
      (form-to [:post "/login"]
               (submit-button "Log in"))))

(defn register [& [error]]
  "Register page"
  (layout/common 
    [:h1 "Registration page! All fields are mandatory"]
    [:p error]
    (form-to [:post "/firstPgReg"]
        [:p "Name:  " (text-field "name")]
        [:p "Mail(example: john@gmail.com):  " (text-field "mail")]
        [:p "Password:  " (password-field "password")]
        [:p "Retype password:  " (password-field "password1")]
        (submit-button "Continue"))
    (form-to [:post "/"]
             (submit-button "Previous page"))))

(defn save-in-database [name mail password]
  "Saves registered user in databae"
  (total.file-content-manipulation/write-html-in-file)
  (total.file-content-manipulation/precisceno)
  (total.file-content-manipulation/for-database)
  (total.file-content-manipulation/fill-atom)
  (total.file-content-manipulation/final-touch)
  (total.models.db/save-user name mail password (slurp "artists1.txt")))
 
(defn first-pg-reg [name mail password password1]
  "First page for registered user"
    (cond
      (or (empty? name) (empty? mail) (empty? password) (empty? password1)) (register "Fill all fields, then click button \"Continue\"")
        (not= password password1) (register "Passwords must match! Try again and press button \"Continue\"") 
        (< (count password) 5) (register "Password length must be more than 5 characters. Try again and press button \"Continue\"")
        (not= (total.models.db/get-user-registration mail password) nil) (register "User with entered credentials already exists in database. Log in or register using other mail. ")
  :else
  (layout/common 
     (save-in-database name mail password)
    [:h1 "Welcome " name "!"]
    [:p "Your clips are: "]
    (text-area {:rows 30 :cols 80} "yt" (slurp "artists1.txt"))    
   ; [:p "Most similar to you according to selected clips from YouTube is user: Natasa"]
   ; [:p "You can connect with that user via e-mail: nata@gmail.com"]
    ;ovde dodaj poruku o gresci ako je korisnik uneo nevalidan broj
    (form-to [:post "/match"]
             (submit-button "Find match")))))

(defn match []
  "Returns user with similar music taste"
  (algorithm/vectors-in-file)
    (layout/common 
 (form-to [:post "/last"]

      [:h1 "Match page!"]
      [:p "Most similar to you according to selected clips from YouTube is user: Natasa"]
      [:p "You can connect with that user via e-mail: nata@gmail.com"]
      [:p "Rate application on the scale from 1.0 (I do not like it) to 5.0 (It's phenomenal): " (text-field "rating") (submit-button "Compare with similar applications")]))
    )

(defn login [& [password error]]
  "Login page"
  (layout/common 
    [:h1 "Login page!"]
    [:p error]
   (form-to [:post "/firstPgLog"]
        [:p "Name:  " (text-field "name")]
        [:p "Password:  " (password-field "password")]
        (submit-button "Continue"))
   (form-to [:post "/"]
             (submit-button "Previous page"))))

(defn first-pg-log [& [name password error]]
"First page for logged user"
(cond
    (or (empty? name) (empty? password)) (login "Fill all fields, then click button \"Continue\"")
    (= (total.models.db/get-user-login name password) nil) (login "User with entered username and password doesn't exist in database. Try again.")    
  :else
  (layout/common 
    [:h1 "Welcome " name "!"]
    
    [:p "Your clips are: "]
    (text-area {:rows 30 :cols 80} "yt" (slurp "artists1.txt"))    
    [:p "Most similar to you according to selected clips from YouTube is user: Natasa"]
    [:p "You can connect with that user via e-mail: nata@gmail.com"]
    [:p error]
    [:p "Rate application on the scale from 1.0 (I do not like it) to 5.0 (It's phenomenal): " (text-field "rating")]
    
    (form-to [:post "/last"]
             (submit-button "Compare with similar applications")
    ))))

;;Dodati ovde multimetode:
(def lastfm
  "Content of page lastfm"
  (slurp "http://appcrawlr.com/ios/last-fm"))

(def spotify
  "Content of page spotify"
  (slurp "http://appcrawlr.com/ios/spotify"))

(def soundcloud
    "Content of page soundcloud"
  (slurp "http://appcrawlr.com/ios/soundcloud"))

(def break
  "After this tag comes rating"
  (str ";color:#333333\">"))

(defn similar-app [app]
  (subs app (+ (.indexOf app break) (count break)) (+ (.indexOf app break) (+ (count break) 3))))

(defn last [rating]
  "Returns rating for each app"
  (layout/common [:h1 "Your rating versus average ratings of similar applications:"]
    (form-to [:post "/logout"]
    [:p "YouTube random jukebox: " rating]
    [:p "LastFM: " (subs lastfm (+ (.indexOf lastfm break) (count break)) (+ (.indexOf lastfm break) (+ (count break) 3)))]
    [:p "Spotify: " (subs spotify (+ (.indexOf spotify break) (count break)) (+ (.indexOf spotify break) (+ (count break) 3)))]
    [:p "Sound Cloud: " (subs soundcloud (+ (.indexOf soundcloud break) (count break)) (+ (.indexOf soundcloud break) (+ (count break) 3)))]
    (submit-button "Log out"))))

(defroutes home-routes
  (GET "/" [] (welcome))
  (POST "/" [] (welcome))
  (POST "/register" [] (register))
  (POST "/match" [] (match))
  (POST "/login" [password] 
        (session/put! :user password)
        (login))
  (POST "/last" [rating] (last rating))
  (POST "/firstPgReg" [name mail password password1] (first-pg-reg name mail password password1))
  (POST "/firstPgLog" [name password] (first-pg-log name password))
  (POST "/logout" [] 
        (session/clear!)
        (welcome)))