(defproject total "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [hiccup "1.0.4"]
                 [ring-server "0.3.0"]
                 [lib-noir "0.7.6"]
                 [midje "1.4.0"]
                 ;;JDBC dependencies
                 [org.clojure/java.jdbc "0.2.3"]
                 [org.xerial/sqlite-jdbc "3.7.2"]]  
  :plugins [[lein-ring "0.8.12"]
            [lein-midje "3.1.3"]]
  :ring {:handler total.handler/app
         :init total.handler/init
         :destroy total.handler/destroy}
  :profiles
  {:uberjar {:aot :all}
   :production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.3.1"]]}})
