(ns bjs.evolutionary-computation.chapter-4-representation-recombination-and-mutation)



;; # Exercises for Chapter Four: Representation, Recombination and Mutation
;; http://www.evolutionarycomputation.org/exercises/exercises-for-chapter-four-representation-recombination-and-mutation/


;; ## 1. A genetic algorithm has individuals coded as binary strings of length 27. Mutation is applied with a bit-wise probability of 1/27. What is the probability that the gene 17 is changed by mutation?
;; - 1/27
;; - 26/27
;; - 1/17
;; - It is not possible to say without knowing what happens to the other genes


;; ### Answer:

;; - You flip a 27 sided dye 27 times, i.e. a Bernoulli-experiment for each bit.
;; - The chance for a bit flipping is given by the <> distribution:






























;; ## 2. A number of on/off switches control a nuclear power plant, and a given configuration can be thought of as a state. It is desired to search the space of possible states to find one that minimises temperature fluctuations within the plant. It is decided to do this with a Genetic algorithm.
;; What representation do you think would be most suitable for this problem if there are n switches?

;; A string of n binary values.
;; A vector of n floating point numbers.
;; A string of values each coming from the set {1,…,n}
;; A tree with n terminal nodes (leaves).
;; For which of the following types of problem representation would it not be suitable to use 2-point crossover?
;; A permutation representing the order in which a series of operations are performed in an operating theatre.
;; A binary string.
;; A sequence of integers representing moves from the set {left, right, ahead}.
;; A vector of floating-point numbers representing angles within a design problem.
;; Which of the following offspring can not be created by one point crossover from two parents 000000 and 111111 ?
;; 111111.
;; 000000
;; 111000
;; 110011
;; 011110
;; 001111
;; A mountain bike designer is trying to create a frame with certain desirable characteristics under simulation. To do this they must specify a set of n tube lengths and m angles between them. What representation do you think would be most suitable for this problem?
;; A string of (n+m) binary values.
;; A string of n values each between 1 and m.
;; A vector of (n+m) floating point numbers.
;; A permutation of the numbers 1 through to (n+m)
;; A tree with (n+m) terminal nodes (leaves).
;; It is necessary to schedule a set of appointments with a doctor so as to minimise the average waiting time per patient. There are n patients. What representation do you think would be most suitable for this problem?
;; A string of n binary values.
;; A vector of n floating point numbers.
;; A string of values, each coming from the set 1 to n.
;; A permutation of the numbers 1 to n.
;; A tree with n terminal nodes (leaves).
