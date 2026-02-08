 (ns bjs.evolutionary-computation.chapter-4-representation-recombination-and-mutation)

;; # Exercises for Chapter Four: Representation, Recombination and Mutation
;; http://www.evolutionarycomputation.org/exercises/exercises-for-chapter-four-representation-recombination-and-mutation/


;; ## 1. A genetic algorithm has individuals coded as binary strings of length 27. Mutation is applied with a bit-wise probability of 1/27. What is the probability that the gene 17 is changed by mutation?
;; - 1/27
;; - 26/27
;; - 1/17
;; - It is not possible to say without knowing what happens to the other genes

;; ### Answer:
;; - saying each gene is a bit in the string,
;; - 'bit-wise probability' is applied per gene, independent of the others
;; - the chance of any of the genes mutating is 1/27


;; ## 2. A number of on/off switches control a nuclear power plant, and a given configuration can be thought of as a state. It is desired to search the space of possible states to find one that minimises temperature fluctuations within the plant. It is decided to do this with a Genetic algorithm.
;; What representation do you think would be most suitable for this problem if there are n switches?

;; - A string of n binary values.
;; - A vector of n floating point numbers.
;; - A string of values each coming from the set {1,…,n}
;; - A tree with n terminal nodes (leaves).

;; ### Answer:
;; A string of n binary values is the natural choice.

;; ## 3. For which of the following types of problem representation would it not be suitable to use 2-point crossover?

;; - A permutation representing the order in which a series of operations are performed in an operating theatre.
;; - A binary string.
;; - A sequence of integers representing moves from the set {left, right, ahead}.
;; - A vector of floating-point numbers representing angles within a design problem.

;; ### Answer:
;; 2 point crossover makes sense for all of them except the first with the ordering.

;; Say
;;
;; individuals:
;;
;; [ a, b, c, d ]
;; [ d, a, b, c ]
;;
;; Then crossover (doesn't matter if 2 point).
;;
;; [ a ] || [ b, c, d ]
;;
;;              ^
;;              |
;;              v
;;
;; [ d ] || [ a, b, c ]
;;
;;
;; =>  [  d, b, c, d  ]   // ❌  invalid individual because the same symbol appears multiple times.
;;
;;
;;


;; Which of the following offspring can not be created by one point crossover from two parents 000000 and 111111 ?

;; 111111
;; 000000
;; 111000
;; 110011
;; 011110
;; 001111

;

;; 111111 ❌
;; 000000 ❌
;; 111000 ❌
;; 110011 ✔
;; 011110 ✔
;; 001111 ❌

;; Since it is 2 point crossover, both ends will come from the same individual. With a 'middle piece' coming from the other individual.


;; ## 4. A mountain bike designer is trying to create a frame with certain desirable characteristics under simulation. To do this they must specify a set of n tube lengths and m angles between them. What representation do you think would be most suitable for this problem?

;; - A string of (n+m) binary values.
;; - A string of n values each between 1 and m.
;; - A vector of (n+m) floating point numbers.
;; - A permutation of the numbers 1 through to (n+m)
;; - A tree with (n+m) terminal nodes (leaves).

;; ### Answer:
;; They all don't make any sense except a vector of (n+m) floating point numbers.
;;

;; ## 5. It is necessary to schedule a set of appointments with a doctor so as to minimise the average waiting time per patient. There are n patients. What representation do you think would be most suitable for this problem?

;; - A string of n binary values.
;; - A vector of n floating point numbers.
;; - A string of values, each coming from the set 1 to n.
;; - A permutation of the numbers 1 to n.
;; - A tree with n terminal nodes (leaves).

;; ### Answer:
;;
;; The problem only makes sense if the patient appointments take varying amount of time.
;; Then, it is very similiar to a knapsack problem with value = constantly 1.
;;
;; This is an ordering problem and a permutation representing the ordering is the most suitable.
;;
