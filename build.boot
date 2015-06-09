(set-env!
 :source-paths #{"src/main/java" "src/test/java"}
 :dependencies '[[radicalzephyr/boot-junit "0.1.0-SNAPSHOT" :scope "test"]])

(def +version+ "0.1.0")

(require '[radicalzephyr.boot-junit :refer [junit]])

(task-options!
 pom {:project 'http-server
      :version +version+
      :description "A Java HTTP server."
      :url "https://github.com/RadicalZephyr/http-server"
      :scm {:url "https://github.com/RadicalZephyr/http-server.git"}
      :licens {"MIT" "http://opensource.org/licenses/MIT"}}
 junit {:packages '#{net.zephyrizing.http_server}})

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
