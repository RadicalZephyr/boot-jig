(set-env!
 :resource-paths #{"resources"})

(require '[clojure.java.io :as io])

(deftask change!
  "Access and potentially change the current directory via fileset operations.

  This task changes the environment, removing all currently set
  source, resource and asset paths, and instead setting the current
  directory as the sole entry in :source-paths.  This puts every file
  in the current directory into the fileset, allowing relative paths
  from the current directory to access them.

  In order to actually change the fileset you still need to put the
  `target` task at the end of your pipeline, but `change!` configures
  it's options to \"do the right thing\" and write the fileset to the
  current directory without cleaning.

  Finally, since it's pretty rare to interact with source control
  repositories as files directly, this task filters out common source
  control files and directory names."
  []
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
