(set-env!
 :resource-paths #{"resources"}
 :source-paths #{"src"}
 :dependencies '[[radicalzephyr/boot-junit "0.2.1"  :scope "test"]
                 [junit                    "4.12"   :scope "test"]])

(def +version+ "0.1.0-SNAPSHOT")

(require '[radicalzephyr.bootlaces  :refer :all]
         '[radicalzephyr.boot-junit :refer [junit]])

(set-env! :resource-paths #{}) ;; Must undo bootlaces! adding "src" to
                               ;; the resource-paths. Will be fixed in 0.1.15

(task-options!
 pom {:project 'radicalzephyr/http-server
      :version +version+
      :description "A Java HTTP server."
      :url "https://github.com/RadicalZephyr/http-server"
      :scm {:url "https://github.com/RadicalZephyr/http-server.git"}
      :licens {"MIT" "http://opensource.org/licenses/MIT"}}
 sift {:invert true
       :include #{#"\.java$"}}
 junit {:classes #{"UnitTests"}})

;;; This prevents a name collision WARNING between the test task and
;;; clojure.core/test, a function that nobody really uses or cares
;;; about.
(if ((loaded-libs) 'boot.user)
  (ns-unmap 'boot.user 'test))

(deftask test
  "Compile and run my jUnit unit tests."
  []
  (comp (javac)
        (junit)))

(deftask build
  "Build my http server."
  []
  (comp (javac)
        (pom)
        (sift)
        (jar)))
