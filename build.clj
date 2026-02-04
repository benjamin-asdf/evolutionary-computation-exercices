(require '[scicloj.clay.v2.api :as clay])


(clay/make!
 {:show false
  :format [:quarto :html]
  :book {:title "Evolutionary Computation Exercises"}
  :clean-up-target-dir true
  :source-path ["src/bjs/evolutionary_computation/index.clj"
                "src/bjs/evolutionary_computation/chapter_2_origins.clj"]})
