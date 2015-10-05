(defproject dappr "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "ISC License"
            :url "https://opensource.org/licenses/ISC"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clj-http  "2.0.0"]
                 [org.clojure/data.json  "0.2.6"]
                 [hiccup  "1.0.5"]
                 [de.ubercode.clostache/clostache  "1.4.0"]]
  :main ^:skip-aot dappr.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
