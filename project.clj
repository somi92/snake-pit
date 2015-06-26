(defproject snake-pit "0.1.0-SNAPSHOT"
  :description "Variant of The Snake game with autonomous snake behavior implemented using genetic programming."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [fungp "0.3.2"]]
  :main ^:skip-aot snake-pit.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
