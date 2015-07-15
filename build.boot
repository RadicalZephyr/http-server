(set-env!
 :source-paths #{"src"}
 :dependencies '[[radicalzephyr/bootlaces  "0.1.14"         :scope "test"]
                 [radicalzephyr/boot-junit "0.2.1-SNAPSHOT" :scope "test"]
                 [junit                    "4.12"           :scope "test"]
                 [net.sf.jopt-simple/jopt-simple "4.9"]])

(def +version+ "0.1.0-SNAPSHOT")

(require '[radicalzephyr.bootlaces  :refer :all]
         '[radicalzephyr.boot-junit :refer [junit]])

(bootlaces! +version+)
(set-env! :resource-paths #{}) ;; Must undo bootlaces! adding "src" to
                               ;; the resource-paths. Will be fixed in 0.1.15

(task-options!
 pom {:project 'http-server
      :version +version+
      :description "A Java HTTP server."
      :url "https://github.com/RadicalZephyr/http-server"
      :scm {:url "https://github.com/RadicalZephyr/http-server.git"}
      :licens {"MIT" "http://opensource.org/licenses/MIT"}}
 jar {:file "server.jar"
      :manifest {"Main-Class" "net.zephyrizing.http_server.HttpServer"}}
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

(deftask test-all
  "Compile and run all jUnit tests."
  []
  (comp (javac)
        (junit :classes #{"UnitTests"
                          "IntegrationTests"})))

(deftask build
  "Build my http server."
  []
  (comp (javac)
        (pom)
        (sift)
        (uber)
        (jar)))
