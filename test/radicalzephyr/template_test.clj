(ns radicalzephyr.template-test
  (:require [radicalzephyr.template :as sut]
            [clojure.test :refer [deftest testing is]]
            [boot.core :as core]))

(deftest test-thing
  (is (= '(deftask build []
            (cljs))
         (sut/cljs-build-task-template [] []))))
