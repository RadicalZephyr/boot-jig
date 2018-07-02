(ns radicalzephyr.template-test
  (:require [radicalzephyr.template :as sut]
            [clojure.test :refer [deftest testing is]]
            [boot.core :as core]))

(deftest test-thing
  (is (= '(deftask build []
            (cljs))
         (sut/cljs-build-task-template [] [])))

  (is (= '(deftask build []
            (task1)
            (task2)
            (cljs))
         (sut/cljs-build-task-template '[(task1) (task2)]
                                       [])))

  (is (= '(deftask build []
            (cljs)
            (after1)
            (after2))
         (sut/cljs-build-task-template []
                                       '[(after1)
                                         (after2)])))

  (is (= '(deftask build []
            (before1)
            (cljs)
            (after1))
         (sut/cljs-build-task-template '[(before1)]
                                       '[(after1)]))))

(deftest test-task-options-templates
  (is (= '(deftask development []
            (task-options! cljs {:optimizations :none
                                 :source-map true})
            identity)
         (sut/cljs-development-task-template
          '{cljs {:optimizations :none
                  :source-map true}}
          {})))

  (is (= '(deftask development []
            (task-options! cljs {:optimizations :none
                                 :source-map true}
                           reload {:asset-path "public"
                                   :on-jsload 'atreus.app/dev-reload})
            identity)
         (sut/cljs-development-task-template
          '{cljs {:optimizations :none
                  :source-map true}}
          '{reload {:asset-path "public"
                    :on-jsload 'atreus.app/dev-reload}})))

  (is (= '(deftask development []
            (task-options! cljs {:optimizations :none
                                 :source-map true
                                 :compiler-options {:devcards true}})
            identity)
         (sut/cljs-development-task-template
          '{cljs {:optimizations :none
                  :source-map true}}
          '{cljs {:compiler-options {:devcards true}}})))

  (is (= '(deftask development []
            (task-options! cljs {:optimizations :none
                                 :source-map true
                                 :compiler-options {:devcards true
                                                    :preloads '[day8.re-frame-10x.preload]}})
            identity)
         (sut/cljs-development-task-template
          '{cljs {:optimizations :none
                  :source-map true
                  :compiler-options {:devcards true}}}
          '{cljs {:compiler-options {:preloads '[day8.re-frame-10x.preload]}}}))))
