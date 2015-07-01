(set-env!
 :source-paths #{"src"}
 :dependencies '[[radicalzephyr/boot-junit "0.1.1" :scope "test"]
                 [jeluard/boot-notify "0.1.2"      :scope "test"]
                 [net.sf.jopt-simple/jopt-simple "4.9"]])

(def +version+ "0.1.0-SNAPSHOT")

(require '[radicalzephyr.boot-junit :refer [junit]]
         '[jeluard.boot-notify      :refer [notify]])

(task-options!
 pom {:project 'http-server
      :version +version+
      :description "A Java HTTP server."
      :url "https://github.com/RadicalZephyr/http-server"
      :scm {:url "https://github.com/RadicalZephyr/http-server.git"}
      :licens {"MIT" "http://opensource.org/licenses/MIT"}}
 jar {:file "server.jar"
      :manifest {"Main-Class" "net.zephyrizing.http_server.HttpServer"}})

;;; This prevents a name collision WARNING between the test task and
;;; clojure.core/test, a function that nobody really uses or cares
;;; about.
(if ((loaded-libs) 'boot.user)
  (ns-unmap 'boot.user 'test))

(deftask test
  "Compile and run my jUnit tests."
  []
  (comp (javac)
        (junit)))

(deftask build
  "Build my http server."
  []
  (comp (javac)
        (pom)
        (jar)))
