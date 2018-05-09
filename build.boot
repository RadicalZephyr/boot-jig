(set-env!
 :resource-paths #{"resources"})

(require '[clojure.java.io :as io])

(deftask change []
  (set-env! :source-paths (fn [_] #{})
            :resource-paths (fn [_] #{})
            :asset-paths (fn [_] #{}))
  (set-env! :source-paths #{"."})
  (task-options! target {:no-clean true :dir #{"."}})
  (sift :include #{#"^.bzr/" #"^.hg/" #"^.git/" #".svn/" #"CVS"}
        :invert true))

(deftask add-file [f file FILE str "Filename to add"
                   c content CONTENT str "Content of the file"
                   a append bool "Should the content be appended?"]
  (let [tmp (tmp-dir!)]
    (with-pre-wrap [fs]
      (empty-dir! tmp)
      (let [in-file (tmp-get fs file)
            out-file (io/file tmp file)]
        (when (and append in-file)
          (io/make-parents out-file)
          (io/copy (tmp-file in-file) out-file))
        (spit out-file content :append append))
      (-> fs
          (add-resource tmp)
          commit!))))
