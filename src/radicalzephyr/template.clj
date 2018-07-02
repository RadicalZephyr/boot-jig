(ns radicalzephyr.template
  (:require [boot.core :as core]))

(defn cljs-build-task-template [before after]
  (core/template
   (deftask build []
     ~@before
     (cljs)
     ~@after)))

(defn- merge-recur [default overrides]
  (merge-with merge-recur default overrides))

(defn cljs-development-task-template [current-options new-options]
  (core/template
   (deftask development []
     (task-options!
      ~@(mapcat identity
                (merge-with merge-recur current-options new-options)))
     identity)))
