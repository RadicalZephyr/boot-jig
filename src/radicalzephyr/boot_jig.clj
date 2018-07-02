(ns radicalzephyr.boot-jig
  (:require [boot.core :as core]
            [clojure.java.io :as io]
            [boot.task.built-in :as task]))

(core/deftask change!
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
  (core/set-env! :source-paths (fn [_] #{})
                 :resource-paths (fn [_] #{})
                 :asset-paths (fn [_] #{}))
  (core/set-env! :source-paths #{"."})
  (core/task-options! task/target {:no-clean true :dir #{"."}})
  (task/sift :include #{#"^.bzr/" #"^.hg/" #"^.git/" #".svn/" #"CVS"}
             :invert true))

(core/deftask add-file [f file FILE str "Filename to add"
                        c content CONTENT str "Content of the file"
                        a append bool "Should the content be appended?"]
  (let [tmp (core/tmp-dir!)]
    (core/with-pre-wrap [fs]
      (core/empty-dir! tmp)
      (let [in-file (core/tmp-get fs file)
            out-file (io/file tmp file)]
        (when (and append in-file)
          (io/make-parents out-file)
          (io/copy (core/tmp-file in-file) out-file))
        (spit out-file content :append append))
      (-> fs
          (core/add-resource tmp)
          core/commit!))))
