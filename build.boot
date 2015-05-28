(set-env!
 :source-paths #{"src/main/java"}
 :dependencies '[[junit "4.12" :scope "test"]])

(def +version+ "0.1.0")

(task-options!
 pom {:project 'http-server
      :version +version+
      :description "A Java HTTP server."
      :url "https://github.com/RadicalZephyr/http-server"
      :scm {:url "https://github.com/RadicalZephyr/http-server.git"}
      :licens {"MIT" "http://opensource.org/licenses/MIT"}})

(deftask junit
  "Run the jUnit test runner."
  []
  )

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
