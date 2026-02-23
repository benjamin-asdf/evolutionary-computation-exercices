(ns bjs.evolutionary-computation.chapter-5-fitness-selection-and-population-management)

;; # Exercises for Chapter Five: Fitness, Selection, and Population Management
;; ---------------------------------------------------

;; ## 1.
;; Discuss whether there is survival of the fittest in a generational EA.
;;
;; - In the sense that 'fit' information survives, that is deep in the logic of EA
;; - If offpring size is the same as population size, then individuals do not survive
;; - If the offpring size is smaller thatn the population size, then some individuals 'survive'.
;; For example in the (μ,λ) Evolution Strategy.

;; ## 2.

;; Given the fitness function f(x) = x2, calculate selection probabilities for Fitness Proportional Selection for the individuals x=1, x=2, x=3.

;; fitnes proportional mechanism

(defn fitness-proportional
  "Returns the probability that an individual is chosen as parent according to Fitness Proportional Selection (FPS) mechanism.
  "
  [fitness-f population-P individual-i]
  (/
   (fitness-f individual-i)
   (reduce + 0 (map fitness-f population-P))))

;; ------------------------------------------------------

(def population [1 2 3])
(def f (fn [x] (* x 2)))

(mapv (partial fitness-proportional f population) [1 2 3])
[1/6 1/3 1/2]

;; ## 3.
;; For the same individuals, calculate the selection probabilities for a transposed fitness function f'(x) = f(x) + 100.

(def f* (fn [x] (+ 100 (f x))))

(mapv (comp float (partial fitness-proportional f* population)) [1 2 3])

[0.32692307 0.33333334 0.33974358]

;;
;; We see how FPS behaves differently when `f` is transposed.
;;

;; ## 4.

;; A generational EA has a population size of 100, uses
;; fitness proportionate selection without elitism, and after t
;; generations has a mean population fitness of 76.0. There is one
;; copy of the current best member, which has fitness 157.0.

;; ### 4a):
;; What is the expectation for the number of copies of the
;; best individual present in the mating pool?

(let [mean-fitness 76.0
      pop-n 100
      best-member-fitness 157.0]
  ;; FPS:
  ;; (/
  ;;  (fitness-f individual-i)
  ;;  (reduce + 0 (map fitness-f population-P)))
  ;;
  ;; =>
  (/ best-member-fitness
     (* pop-n mean-fitness)))
0.020657894736842104

;;
;; Answer is ca 2%
;;

;; ### 4b):
;; What is the probability that there will be no
;; copies of that individual in the mating pool, if
;; selection is implemented using the roulette wheel
;; algorithm?

;; Roulette wheel spins the wheel independently for each of the 100 parent slots.
;; P(not picked in one spin) = 1 - p
;; P(not picked in any of 100 spins) = (1 - p)^100

(let [mean-fitness 76.0
      pop-n 100
      best-member-fitness 157.0
      p (/ best-member-fitness (* pop-n mean-fitness))]
  (Math/pow (- 1 p) pop-n))
0.12400599206358769
;; => i.e. about 12.4%

(defn roulette-wheel-selection*
  [cumulative-probabilities point]
  (first (keep-indexed (fn [i prob-i]
                         (when (<= point prob-i) i))
                       cumulative-probabilities)))

(defn roulette-wheel
  "Returns `lambda-members` from a maiting pool (`parents`) according to roulette wheel mechanism.
  `probabilities`: A list of probabilities for each member to be picked as parent.
  "
  [parents probabilities {:keys [lambda-members]}]
  (let [cumulative-probabilities (reductions + probabilities)
        pointers (repeatedly rand)]
    (->> pointers
         (take lambda-members)
         (map #(roulette-wheel-selection* cumulative-probabilities %))
         (mapv parents))))

;; ### 4c):
;; What is the probability if the implementation uses SUS?

;; With SUS, there is a single random start point r in [0, 1/N),
;; then 100 equally spaced pointers at r, r+1/N, r+2/N, ...
;; Pointer spacing = 1/N = 1/100 = 0.01
;;
;; The best individual's segment on the wheel has width
;; p ≈ 0.02066, which is *wider* than the pointer spacing (0.01).
;;
;; When a segment is wider than the pointer spacing, at least
;; floor(p * N) pointers are guaranteed to land in it.
;; floor(0.02066 * 100) = floor(2.066) = 2
;;
;; So with SUS the probability of 0 copies is **0**.
;; The best individual is guaranteed at least 2 copies.
;;
;; This is a key advantage of SUS: it eliminates the stochastic
;; sampling error that gives roulette wheel a ~12.4% chance of
;; losing the best individual entirely.

(defn stochastic-universal-sampling
  "Returns `lambda-members` from a maiting pool (`parents`) according to stochastic universal sampling (SUS).

  `probabilities`: A list of probabilities for each member to be picked as parent.

  This is like  [[roulette-wheel]], but instead of spinning the wheel lambda times, it is conceptually like
  spinning a single wheel with lambda equally spaced arms.

  "
  [parents probabilities {:keys [lambda-members]}]
  (let [cumulative-probabilities (reductions + probabilities)
        r (rand (/ 1 lambda-members))
        pointers (iterate #(+ % (/ 1 lambda-members)) r)]
    (->> pointers
         (take lambda-members)
         (map #(roulette-wheel-selection* cumulative-probabilities %))
         (mapv parents))))

;; ## 5.

;; You are given the fitness function f(x) = x^2 + 10 and a population of
;; three individuals {a,b,c}. When decoded, their genes give the values
;; 1, 2 and 3 respectively. When you pick a single parent using Fitness
;; Proportionate Selection, what is the probability that it is b?

;; f(a) = 1 + 10 = 11
;; f(b) = 4 + 10 = 14
;; f(c) = 9 + 10 = 19
;; total = 44

;; P(b) = 14 / (11 + 14 + 19) = 14/44 = 7/22

(let [f (fn [x] (+ (* x x) 10))]
  (fitness-proportional f [1 2 3] 2))
;; => 7/22

;; So the answer from the multiple choice is: 14/(11+14+19)


;; ## 6. Calculate the probabilities of selecting b via FPS if f’(x) = x^2

;; f’(a) = 1, f’(b) = 4, f’(c) = 9
;; total = 14
;; P(b) = 4/14 = 2/7

(let [f’ (fn [x] (* x x))]
  (fitness-proportional f’ [1 2 3] 2))
;; => 2/7

;; Note: removing the +10 offset makes selection pressure *stronger*,
;; because the fitness differences are now larger relative to the total.
;; Compare: 7/22 ≈ 0.318 (with +10) vs 2/7 ≈ 0.286 (without).
;; b is less fit than average, so it gets selected *less* without the offset.

;; ---

;; ## 7.
;; What is the probability of selecting b via binary tournament selection?
;; How does it change when the fitness function has its values reduced by 10?

(defn tournament-selection
  "Returns `lambda-members` from mating pool `parents` according to tournament mechanism.
  `comparer`: A function for ordering individuals. Default [[clojure.core/compare]].
  `tournament-size-k`: How many individuals to pick for comparison.
  `criterion`: Optionally a function from individual to some meassure for `comparer`. Default [[identity]]."
  [parents
   {:keys [lambda-members criterion tournament-size-k comparer]
    :or {comparer compare criterion identity tournament-size-k 5}}]
  (let [tournament (fn []
                     (let [tournament-set (take tournament-size-k
                                                (shuffle parents))
                           sorted (sort-by criterion
                                           (fn [a b] (comparer b a))
                                           tournament-set)]
                       (first sorted)))]
    (repeatedly lambda-members tournament)))



;; Binary tournament: pick 2 individuals uniformly at random (with replacement),
;; the fitter one wins.
;;
;; b wins when:
;;   - b vs a  (b wins, since f(b) > f(a))
;;   - b vs b  (b wins trivially)
;; b loses when:
;;   - b vs c  (c wins, since f(c) > f(b))
;;
;; All 9 possible pairings where b is one of the two selected:
;;   P(b is picked) = 1/3
;;   Given b is picked, the opponent is a, b, or c each with prob 1/3
;;
;; But more precisely, b wins the tournament if b is selected AND
;; b beats the opponent. We need P(b wins a tournament):
;;
;; Enumerate all 9 equally likely (i,j) pairs:
;;   (a,a)->a  (a,b)->b  (a,c)->c
;;   (b,a)->b  (b,b)->b  (b,c)->c
;;   (c,a)->c  (c,b)->c  (c,c)->c
;;
;; b wins in 3 out of 9 cases => P(b) = 3/9 = 1/3
;;
;; This does NOT change when the fitness function is shifted by -10,
;; because tournament selection only depends on the *ranking* of
;; individuals, not on absolute fitness values.
;; f(x) = x^2 + 10 and f(x) = x^2 both give ranking a < b < c.
