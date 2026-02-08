(ns build
  (:require [scicloj.clay.v2.api :as clay]))

(defn build [_]
  (clay/make!
   {:show false
    :format [:quarto :html]
    :book {:title "Evolutionary Computation Exercises"}
    :clean-up-target-dir true
    :base-target-path "docs"
    :source-path ["src/bjs/evolutionary_computation/index.clj"
                  "src/bjs/evolutionary_computation/chapter_2_origins.clj"]})
  (shutdown-agents)
  (System/exit 0))
