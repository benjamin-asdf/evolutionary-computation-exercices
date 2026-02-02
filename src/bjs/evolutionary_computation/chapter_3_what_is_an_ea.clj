(ns bjs.evolutionary-computation.chapter-3-what-is-an-ea)

;; # Exercises for Chapter Three: What is an EA?

;; ## 1. How big is the phenotype space for the eight-queens problem?

;; - considering the phenotype space to be all possible ways to place 8 queens on a chess board.

(def chess-board-positions (* 8 8))
(def queens 8)

;; - Obviously, for the first queen, there are 8x8 = 64 possible positions,
;; - Second queen 64 - 1 possiblities, etc.
;; - The total number is the Binomial coefficient https://en.wikipedia.org/wiki/Binomial_coefficient

(defn binomial-coefficient [n k]
  (if (or (< k 0) (> k n))
    0
    (let [k (min k (- n k))]
      (loop [numerator 1
             denominator 1
             i 0]
        (if (< i k)
          (recur (* numerator (- n i))
                 (* denominator (inc i))
                 (inc i))
          (/ numerator denominator))))))

(def phenotype-space-size (binomial-coefficient chess-board-positions queens))

phenotype-space-size

;; # 2. eight-queens design

;; Try to design an incremental evolutionary algorithm for the eight-queens problem.
;; That is, a solution must represent a way to place the queens
;; on the chess board one by one. How big is the search space in your design?

;; I did something similiar here: https://benjamin-schwerdtner.de/eight-queens-core-logic.html

;; representation:
;;
;; permutation <0...7> of length 8
;; 1 number per row
;; so that [0,1,2,3,4,5,6,7]  is placing all queens on a diagonal (everybody attacks everybody)
;;

;; - With tis representation, the search space size is simply 8! (factorial 8)
;; - The search space is constrainted, because every column / row is used exactly once.

(defn factorial [n]
  (if (<= n 1)
    1
    (* n (factorial (dec n)))))

(def search-space-size (factorial 8))
search-space-size

;; -------------------------------

;; Incrmental evolutionary algorithm design:

;; - define quality function: number of non-attacking pairs of queens

(declare quality)

;; - define mutation: swap two random positions in the permutation

(declare mutate)

;; - Initialize population with random permutations of [0...7] (population size 10)
;;
;; Until solution found or max generations reached:
;;
;;  - Evaluate quality of each individual using quality function
;;  - Select parents based on quality (top-k selection with k=4)
;;  - Create offspring by mutating parents
;;  - Replace worst individuals in population with offspring (drop worst 2)

;; Impl:

(defn quality
  "Count the number of queens attacking each other in the given board configuration.
   board-config is a vector of column positions, one per row.
   Returns the number of pairs of queens that attack each other."
  [board-config]
  (let [n (count board-config)]
    (loop [row1 0
           conflicts 0]
      (if (>= row1 n)
        conflicts
        (let [col1 (nth board-config row1)
              new-conflicts (loop [row2 (inc row1)
                                   acc 0]
                              (if (>= row2 n)
                                acc
                                (let [col2 (nth board-config row2)
                                      ;; Check diagonal attacks
                                      diag-attack? (= (Math/abs (- row1 row2))
                                                      (Math/abs (- col1 col2)))]
                                  (recur (inc row2)
                                         (if diag-attack? (inc acc) acc)))))]
          (recur (inc row1) (+ conflicts new-conflicts)))))))

(defn mutate
  "Swap two random positions in the permutation."
  [board-config]
  (let [n (count board-config)
        i (rand-int n)
        j (rand-int n)]
    (if (= i j)
      (recur board-config) ; ensure we swap different positions
      (assoc board-config i (board-config j) j (board-config i)))))

(defn random-permutation
  "Generate a random permutation of [0...n-1]."
  [n]
  (vec (shuffle (range n))))

(defn create-population
  "Create initial population of random permutations."
  [pop-size board-size]
  (repeatedly pop-size #(random-permutation board-size)))

(defn evaluate-population
  "Evaluate all individuals and return pairs of [individual quality]."
  [population]
  (map (fn [ind] [ind (quality ind)]) population))

(defn select-parents
  "Select top-k individuals with lowest conflict count."
  [evaluated-pop k]
  (->> evaluated-pop
       (sort-by second)  ; sort by quality (lower conflicts = better)
       (take k)
       (map first)))

(defn evolve-generation
  "Perform one generation of evolution."
  [{:keys [population pop-size num-parents num-offspring]}]
  (let [evaluated (evaluate-population population)
        parents (select-parents evaluated num-parents)
        offspring (repeatedly num-offspring #(mutate (rand-nth (vec parents))))
        ;; Keep best individuals and add offspring
        survivors (->> evaluated
                       (sort-by second)
                       (take (- pop-size num-offspring))
                       (map first))
        new-pop (concat survivors offspring)]
    {:population (vec new-pop)
     :pop-size pop-size
     :num-parents num-parents
     :num-offspring num-offspring
     :best (first (sort-by second evaluated))}))

(defn run-ea
  "Run the evolutionary algorithm until solution found or max generations reached."
  [& {:keys [pop-size num-parents num-offspring max-generations board-size]
      :or {pop-size 10
           num-parents 4
           num-offspring 2
           max-generations 1000
           board-size 8}}]
  (loop [state {:population (vec (create-population pop-size board-size))
                :pop-size pop-size
                :num-parents num-parents
                :num-offspring num-offspring}
         generation 0]
    (let [{:keys [best] :as new-state} (evolve-generation state)
          [best-ind best-quality] best]
      (cond
        (zero? best-quality)
        {:solution best-ind
         :generation generation
         :status :solved}

        (>= generation max-generations)
        {:best-found best-ind
         :best-quality best-quality
         :generation generation
         :status :max-generations-reached}

        :else
        (recur new-state (inc generation))))))

(comment
  (repeatedly 10 run-ea)

  '({:generation 3 :solution [2 7 3 6 0 5 1 4] :status :solved}
    {:best-found [7 1 4 6 0 3 5 2]
     :best-quality 1
     :generation 1000
     :status :max-generations-reached}
    {:best-found [2 5 7 4 0 3 6 1]
     :best-quality 1
     :generation 1000
     :status :max-generations-reached}
    {:generation 38 :solution [3 7 4 2 0 6 1 5] :status :solved}
    {:generation 36 :solution [2 5 3 1 7 4 6 0] :status :solved}
    {:generation 26 :solution [5 2 0 6 4 7 1 3] :status :solved}
    {:generation 43 :solution [3 5 7 1 6 0 2 4] :status :solved}
    {:generation 130 :solution [5 3 0 4 7 1 6 2] :status :solved}
    {:generation 19 :solution [3 6 0 7 4 1 5 2] :status :solved}
    {:generation 41 :solution [6 2 0 5 7 4 1 3] :status :solved})


  )

;; -------------------------------

;; ## 3. Knapsack problem - Naive vs Decoder approach

;; Explain why the order in which items are listed in the representation is
;; unimportant for the naive approach to the knapsack problem, but makes a
;; big difference if we use the decoder approach.

;; ----------------------------------------------------

;; Problem:
;; - list of n items, each with a volume and value.
;; - Capacity C of the 'backpack'.
;; - We need to maximize the value while not exceeding capacity.

;; Representation (genotype0:
;; binary string of length n, where 1 represents the item is included and 0 that it is omitted.
;;

;; **Naive approach:**
;; - phenotype == genotype space, (i.e. all 2^n bit strings)
;; - then phenotype space includes invalid solutions, those where capacity is exceeded.

;; **Decoder approach:**
;; - phenotype = decoder(genotype)
;; - decoder is a function:
;;   - walk the genotype from left to right,
;;   - if putting the next item into the backpack does not exceed capacity, put it
;;   - else omit it.
;; - G->P mapping of genotype to phenotype is many to one, because once capacity is reached,
;;   the rest of the bits don't matter for the solution (which items are included).
;;
;;

;; items: A list of tuples [value, volume]
;; capacity: knapsack C
;; phentotype: bit string of which items are included
(defn genotype-decoder
  [capacity items phenotype]
  (let [volumes (mapv second items)]
    (reduce (fn [included-items-spec phenotype-bit]
              (let [used-capacity
                    (apply + (map #(* %1 %2) included-items-spec volumes))
                    include? (<= (+ used-capacity
                                   (* phenotype-bit
                                      (volumes (count
                                                included-items-spec))))
                                capacity)]
                (conj included-items-spec (if include? 1 0))))
      []
      phenotype)))

(genotype-decoder 5 [[1 5] [1 1] [1 1] [1 1]] [1 1 1])
[1 0 0]

(genotype-decoder 5 [[1 4] [1 1] [1 1] [1 1]] [1 1 1])
[1 1 0]

;; -------------------------------

;; ## 4. Problem where EAs perform poorly

;; Find a problem where EAs would certainly perform very poorly compared to
;; alternative approaches. Explain why you expect this to be the case.

;; **Example: Sorting a list of numbers**
;;
;; EAs would perform terribly on sorting because:
;;
;; 1. **Deterministic optimal solution exists**: Sorting algorithms like
;;    quicksort, mergesort achieve O(n log n) with certainty
;;
;; 2. **No noise or uncertainty**: The problem has no stochastic elements
;;    where population-based search would help
;;
;; 3. **Well-understood structure**: The problem structure is fully known,
;;    no need for search/discovery
;;
;; 4. **Gradient-like information available**: We can measure "sortedness"
;;    precisely and fix violations directly
;;
;; 5. **Massive search space**: n! permutations makes random search hopeless
;;    for large n, while divide-and-conquer is efficient
;;
;; Similarly: matrix multiplication, shortest path (use Dijkstra),
;; linear programming (use simplex), any problem with known polynomial
;; algorithm exploiting problem structure.

;; -------------------------------

;; ## 5. Exploration vs Exploitation phases

;; Consider the beginning phase of an evolutionary search process.
;; Is exploration or exploitation the dominant force in this stage?
;; What about the end phase?

;; **Early stage (beginning):**
;; - EXPLORATION dominates
;; - Population is randomly initialized, spread across search space
;; - High diversity means many different regions are being sampled
;; - Selection pressure hasn't yet concentrated the population
;; - Mutations explore broadly from diverse starting points

;; **Late stage (end):**
;; - EXPLOITATION dominates
;; - Population has converged toward promising region(s)
;; - Most individuals are similar (low diversity)
;; - Small mutations fine-tune solutions within local area
;; - Selection maintains and refines good solutions
;; - Risk of premature convergence if stuck in local optimum

;; The balance between exploration and exploitation is fundamental to EA
;; design - too much exploration wastes effort on poor solutions, too
;; much exploitation leads to premature convergence.
