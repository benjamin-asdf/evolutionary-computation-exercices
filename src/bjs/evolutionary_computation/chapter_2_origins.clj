(ns bjs.evolutionary-computation.chapter-2-origins
  (:require [scicloj.kindly.v4.kind :as kind]))

;; # Exercises for Chapter Two: EC Origins

;; ## 1. Find out when hominids are first thought to have appeared, and estimate how many generations it has taken for you to evolve.

;; https://en.wikipedia.org/wiki/Australopithecine

;; Hominini / Australopithecus earliest roughly 5.5 million years ago.

(def hominins-age 5.5e6)
;; typically used in population genetics etc. for homo.
(def homo-generation-time 25)

;; ~ Answer in thousands of generations:
(/ hominins-age homo-generation-time 1e4)

;; ## 2. Find out the biological definition of evolution and give at least one example of how the term is frequently used in nonbiological settings.

;; **Biological definition:**: Evolution is the change in the heritable characteristics of biological populations over successive generations.

(kind/image {:src "https://upload.wikimedia.org/wikipedia/commons/4/43/Darwin_Tree_1837.png"})

;;
;; The entity described by evolution is a *tree of life*. There exists one tree of life per origin of life event.
;;

;;
;; **Abstract replicator theory definition:**: Evolution is the non-random selection of random replicators.
;;

;; **Non-biological usage:**
;;
;; - Cosmological evolution: The history of the physical universe across time. I.e. starting with the big bang, the description of successive phases with key physical attributes, ending with the end of the universe (perhaps 'Big Crunch', 'Big Freeze', 'Big Rip', ...).
;;
;;
;; - "The evolution of programming languages" — describes gradual change/development over time, but lacks key biological mechanisms (no random mutation, no differential reproduction, no inheritance).
;;

;; ## 3. Table 2.1 suggests a close analogy between the notion of fitness in natural evolutionary systems and a solution quality measure in (evolutionary) problem solving. However, the two work `other way around’ from the perspective of the number of offspring. Discuss.

;; I would say in biological evolution, fitness is the 'causal efficacy' (german Wirkunsmächtigkeit) of a replicator to replicate, given an environment.

(defn fitness
  "Returns a real value describing the causal efficacy of a replicator to replicate given an environment.
  A higher value means higher efficacy.
  "
  [individual environment])

;; Fitness is easily understood as the height of a *fitness landscape* (Wrights adaptive landscape), where a higher value corresponds to higher reproductive success.
;; A branch of the evolutionary process is 'climbing up the mountains' of the landscape.
;; This is the mountain which appears in the title of Dawkins' "Climbing Mount Improbable" (1996).
;;

;; In evolutionary problem solving, a *quality measure* for a candidate solution, given a problem is used to determine the number of offspring in successive generations of solutions.
;;

(defn quality
  "Returns a real value that is an error meassurement of a candidate solution given a problem.
  A lower value means less error, and higher quality."
  [individual problem])

;; 'The other way around' refers to the fact that in computational problem solving, the quality is usually an error to be minimized.
;; The algorithm is descending into the valleys rather than climbing the mountains.
;; I imagine a ball that rolls down the slopes. Dito in dynamical systems theory; They speak of 'falling into' an attractor state of a system.
;;
;; Obviously this sign reversal is trivial and does not affect the structure of the algorithm.
;;

;; ------------------------------------------
;;
;; Claude thinks the question is asking about **causal direction**:
;;
;; | Biology                          | EC                                      |
;; |----------------------------------|-----------------------------------------|
;; | Fitness → offspring count        | Offspring count ← quality               |
;; | Causality is *emergent*          | Causality is *imposed*                  |
;;
;; In biology, having more offspring is what *defines* higher fitness (measurement after the fact).
;; In EC, we *decide* how many offspring to assign based on our quality measure (prescription).
;;
;; - Biology: offspring count is the dependent variable (effect)
;; - EC: offspring count is the independent variable (we control it)

;; ------------------------------------------

;; I don't agree with this. For me, fitness in biology is an objective property of the replicator. In principle, it does not require a meassurment to know a fitness.

;; ##  4.Find out for how long have humans have used the following as tools: Evolution, The wheel, Fire.

;; **Evolution (as a tool)**:
;; - Selective breeding/domestication: ~10,000-15,000 years (dogs ~15,000-30,000 years ago, agriculture ~10,000 years ago)
;; - Evolutionary computation: ~80 years (1940s-1960s saw early evolutionary algorithms)

;; **The Wheel**:
;; - ~5,500-6,000 years (first as potter's wheel ~3500 BCE, then for transport)
;; - Oldest known wheel: Ljubljana marshes, Slovenia, ~5,200 years old

;; **Fire**:
;; - Use of fire (carrying embers from wildfires): ~1-2 million years (Homo erectus)
;; - Controlled fire-making: ~400,000 years (recent 2025 discovery in Britain - early Neanderthals using pyrite + flint)
;; - Cooking: ~780,000 years (Daughters of Jacob Bridge, Israel)

;; Ranking by age: Fire >> Evolution (breeding) >> Wheel >> Evolution (computation)

;; Sources:
;; - https://en.wikipedia.org/wiki/Control_of_fire_by_early_humans
;; - https://www.npr.org/2025/12/11/nx-s1-5640109/early-humans-fire-making-oldest-discovery-archaeology
;; - https://en.wikipedia.org/wiki/Selective_breeding
;; - https://en.wikipedia.org/wiki/Wheel

(comment
  (require '[scicloj.clay.v2.api :as clay])
  (clay/make! {:source-path "src/bjs/evolutionary_computation/chapter_2_origins.clj"}))
