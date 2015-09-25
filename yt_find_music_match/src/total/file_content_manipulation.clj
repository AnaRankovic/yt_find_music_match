(ns total.file-content-manipulation
  (:require [compojure.core :refer :all]
            [total.views.layout :as layout]
            [hiccup.form :refer :all]
            [noir.session :as session]
            [clojure.java.io :as io])
  (:use clojure.java.io
        clojure.java.browse))

(defn count-substring [txt sub]
  "Counts substrings"
     (count (re-seq (re-pattern sub) txt)))

(defn for-database []
  "Writes in file name and number of appearance for each artist"
  (with-open [rdr (reader "artists.txt")]
    (doseq [line (line-seq rdr)]
      (spit "me.txt" (str line " - "(count-substring (slurp "artists.txt") line) "\n") :append true))))

(def artists-number
  "Number of artists"
  (atom #{}))

(defn fill-atom []
  "Atom gets values"
  (with-open [rdr (reader "me.txt")]
    (doseq [line (line-seq rdr)]
      (swap! artists-number conj line))))

(defn final-touch []
  "Atom content gets written in file"
  (spit "artists1.txt" 
        @artists-number))

(defn rand-seq-elem [sequence]
  "Selects arbitrary line"
  (let [f (fn [[k old] new]
            [(inc k) (if (zero? (rand-int k)) new old)])]
    (->> sequence (reduce f [1 nil]) second)))

(defn rand-line [filename]
  "Reads content of forwarded file and calls method which chooses random line"
  (with-open [reader (io/reader filename)]
       (rand-seq-elem (line-seq reader))))

(def break-tag
  "String after which comes YouTube link"
  (str "\" dir=\"ltr\">"))

(def break-tag2
  "String before which comes YouTube link"
  (str "</a><span class=\"accessible-description"))

(def crtica
  (str " - "))

(defn precisceno []
  "Writes artists in file"
  (with-open [](doseq [line (line-seq (reader "clips.txt"))]
                 (if(.contains line crtica)
                   (println (subs line 0 (.indexOf line crtica))
          (spit "artists.txt" (str (subs line 0 (.indexOf line crtica)) "\n") :append true))))))

(defn write-youtube-links [yt-link]
  "Writes YouTube links in file"
  (spit "clips.txt" 
;        (str (subs yt-link 0 (.indexOf yt-link crtica)) "\n**********\n") :append true)
        (str yt-link "\n**********\n") :append true)
  )

(defn write-html-in-file []
  "Writes YouTube page source in file"
  (spit "youtube-html.txt" (slurp "https://www.youtube.com/"))
  (with-open [](doseq [line (line-seq (reader "youtube-html.txt"))]
                 (if(.contains line break-tag2)
                   (println (subs line (+ (.indexOf line break-tag) (count break-tag)) (.indexOf line break-tag2))
                   (write-youtube-links (subs line (+ (.indexOf line break-tag) (count break-tag)) (.indexOf line break-tag2))))))))

(defn delete-youtube-links-file []
  "Deletes file with YouTube links"
  (io/delete-file "youtube-html.txt")
  (io/delete-file "artists.txt")
  (io/delete-file "artists1.txt")
  (io/delete-file "clips.txt")
  (io/delete-file "me.txt")
  (io/delete-file "vektori.txt"))