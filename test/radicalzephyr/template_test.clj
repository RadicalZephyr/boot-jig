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
