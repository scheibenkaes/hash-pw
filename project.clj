(defproject hash-pw "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2311"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [om "0.7.1"]]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]]

  :source-paths ["src"]

  :cljsbuild {
              :builds [{:id "hash-pw"
                        :source-paths ["src"]
                        :compiler {
                                   :output-to "hash_pw.js"
                                   :output-dir "out"
                                   :optimizations :none
                                   :source-map true}}
                       {:id "prod"
                        :source-paths ["src"]

                        :compiler {
                                   :externs ["resources/externs/react.externs.js"]
                                   :output-to "hash_pw.js"
                                   :optimizations :advanced
                                   :pretty-print false}}]})
