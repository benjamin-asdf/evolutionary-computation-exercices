(ns bjs.evolutionary-computation.index
  (:require [scicloj.kindly.v4.kind :as kind]))

;; # Evolutionary Computation

;; Exercises and notes from studying evolutionary computation.

;; ## Chapters

;; - [Chapter 2: EC Origins](bjs.evolutionary_computation.chapter_2_origins.html)

(comment
  (require '[scicloj.clay.v2.api :as clay])
  ;; Build index
  (clay/make! {:source-path "src/bjs/evolutionary_computation/index.clj"})
  ;; Build all notebooks
  (clay/make! {:source-path ["src/bjs/evolutionary_computation/index.clj"
                             "src/bjs/evolutionary_computation/chapter_2_origins.clj"]}))
