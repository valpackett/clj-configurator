(defproject clj-configurator "0.1.5-SNAPSHOT"
  :description "A powerful yet simple configuration library"
  :url "https://github.com/myfreeweb/clj-configurator"
  :license {:name "WTFPL"
            :url "http://www.wtfpl.net/about/"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [coercer "0.2.0"]]
  :profiles {:dev {:dependencies [[midje "1.5.0"]
                                  [lein-release "1.0.0"]
                                  [clj-toml "0.3.0"]]}}
  :plugins [[lein-midje "3.0.0"]
            [lein-release "1.0.0"]]
  :bootclasspath true
  :lein-release {:deploy-via :lein-deploy}
  :repositories [["snapshots" {:url "https://clojars.org/repo" :creds :gpg}]
                 ["releases"  {:url "https://clojars.org/repo" :creds :gpg}]])
