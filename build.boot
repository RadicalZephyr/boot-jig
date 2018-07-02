(def project  'radicalzephyr/boot-jig)
(def version "0.0.0-SNAPSHOT")

(set-env!
 :resource-paths #{"src" "resources"}
 :dependencies (template
                [[org.clojure/clojure ~(clojure-version) :scope "provided"]
                 [boot/core "2.8.1" :scope "provided"]
                 [metosin/bat-test "0.4.0" :scope "test"]]))

(require '[clojure.java.io :as io]
         '[metosin.bat-test :refer [bat-test]])

(task-options! pom {:project     project
                    :version     version
                    :description "A programmable software jig for boot projects."
                    :url         "https://github.com/RadicalZephyr/boot-jig"
                    :scm         {:url "https://github.com/RadicalZephyr/boot-jig"}
                    :license     {"Eclipse Public License"
                                  "http://www.eclipse.org/legal/epl-v10.html"}})
