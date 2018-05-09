(require '[clojure.java.io :as io])

(deftask change []
  (target :no-clean true :dir #{"."}))

(deftask add-file [f file FILE str "Filename to add"
                   c content CONTENT str "Content of the file"
                   a append bool "Should the content be appended?"]
  (let [env (-> (get-env)
                (assoc :src-paths #{"."}))
        tmp (tmp-dir!)]
    (with-pre-wrap [fs]
      (empty-dir! tmp)
      (spit (io/file tmp file) content)
      (-> fs
          (add-resource tmp)
          commit!))))
