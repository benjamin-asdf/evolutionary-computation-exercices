(require '[scicloj.clay.v2.api :as clay])

(clay/make!
 {
  ;; :show false
  :clean-up-target-dir true
  :show false
  :source-path ["src/bjs/evolutionary_computation/index.clj"
                "src/bjs/evolutionary_computation/chapter_2_origins.clj"]})
