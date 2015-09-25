(ns total.models.db
    (:require [clojure.java.jdbc :as sql])
    (:import java.sql.DriverManager))

(def db
  "Definition of database total"
  {:classname "org.sqlite.JDBC",
         :subprotocol "sqlite",
         :subname "total.sqlite"})

(defn save-user [name mail password clips]
  "Saves user in database"
  (sql/with-connection
    db
    (sql/insert-values
      :total
      [:name :mail :password :clips]
      [name mail password clips])))

(defn get-user-login [name password]
  "Selects user with given name and password"
  (sql/with-connection
  db
  (sql/with-query-results res
    ["select * from total where name = ? and password = ?" name password] (first res))))

(defn vrati-sve [id]
  "Select user with given id"
  (sql/with-connection
  db
  (sql/with-query-results res
    ["select * from total where id = ?" id] (first res))))

(defn get-user-registration [mail password]
    "Select user with given mail and password"
  (sql/with-connection
  db
  (sql/with-query-results res
    ["select * from total where mail = ? and password = ?" mail password] (first res))))

(defn create-total-table []
    "Creates table user"
  (sql/with-connection
    db
    (sql/create-table
      :total
      [:id "INTEGER PRIMARY KEY AUTOINCREMENT"]
      [:name "TEXT"]
      [:mail "TEXT"]
      [:password "TEXT"]
      [:clips "TEXT"])))