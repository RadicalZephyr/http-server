(set-env!
 :source-paths #{"src"})

(def +version+ "0.1.0")

(task-options!
 pom {:project 'http-server
      :version +version+
      :description "A Java HTTP server."
      :url "https://github.com/RadicalZephyr/http-server"
      :scm {:url "https://github.com/RadicalZephyr/http-server.git"}
      :licens {"MIT" "http://opensource.org/licenses/MIT"}})

(deftask build
  "Build my http server."
  []
  (comp (javac)
        (pom)
        (jar)))
