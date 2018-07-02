(ns radicalzephyr.template
  (:require [boot.core :as core]))

(defn cljs-build-task-template [before after]
  (core/template
   (deftask build []
     ~@before
     (cljs)
     ~@after)))
