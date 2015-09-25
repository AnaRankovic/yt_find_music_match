(ns total.algorithm
  (:require [total.models.db :as db]
                        [clojure.java.io :as io]))


(def atom1
  "Defines sum of scores of each artist for first user"
  (atom 0))

(def atom2
    "Defines sum of scores of each artist for second user"
  (atom 0))

(def atom3 
    "Defines sum of scores of each artist for third user"
  (atom 0))

(def atom4 
    "Defines sum of scores of each artist for fourth user"
(atom 0))

(def atom5 
    "Defines sum of scores of each artist for fifth user"
(atom 0))

(def atom6 
    "Defines sum of scores of each artist for sixth user"
(atom 0))

(def atom7 
    "Defines sum of scores of each artist for seventh user"
(atom 0))

(def atom8 
    "Defines sum of scores of each artist for eigth user"
(atom 0))

(defn parse-int [s]
  "Convers string to int"
      (Integer. (re-find  #"\d+" s )))

(defn broj-pojavljivanja [i artist]
  "Numbers of appearance of each artist at all users"
(if (.contains (str (total.models.db/vrati-sve i)) artist)
                    (parse-int (subs (str (total.models.db/vrati-sve i)) 
                          (+ (.indexOf (str (total.models.db/vrati-sve i)) artist) (+ (count artist)3))
                          (+ (.indexOf (str (total.models.db/vrati-sve i)) artist) (+ (count artist) 5))))
                    0))

(defn vraca-sve-iz-baze[artist]
  "Returns numbers of appearance of each artist for all users"
  (for [i [1 2 3 4 5 6 7 8]] 
        (broj-pojavljivanja i artist)))

(defn vectors-in-file []
  "Writes vector of artist appearance in file"
  (for [i (distinct (clojure.string/split-lines (slurp "artists.txt")))]
                                      (spit "vektori.txt" (str (vector (vraca-sve-iz-baze i)) "\n") :append true)))

(defn values []
  "Reads values from txt file, and each atom gets for value sum of ponders for each user"
  (vectors-in-file)
                      (with-open [rdr (clojure.java.io/reader "vektori.txt")]
                         (doseq [line (line-seq rdr)]
                           (def no-space (clojure.string/replace line " " ""))
                           (swap! atom1 #(+ % (parse-int (subs no-space 2 3))))
                           (swap! atom2 #(+ % (parse-int (subs no-space 3 4))))
                           (swap! atom3 #(+ % (parse-int (subs no-space 4 5))))
                           (swap! atom4 #(+ % (parse-int (subs no-space 5 6))))
                           (swap! atom5 #(+ % (parse-int (subs no-space 6 7))))
                           (swap! atom6 #(+ % (parse-int (subs no-space 7 8))))
                           (swap! atom7 #(+ % (parse-int (subs no-space 8 9))))
                           (swap! atom8 #(+ % (parse-int (subs no-space 9 10))))
                           )))