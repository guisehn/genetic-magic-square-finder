# Genetic Magic Square Finder

[⬇️ Download executable](https://github.com/guisehn/genetic-magic-square-finder/releases/latest)

[🇧🇷 Versão em português](https://github.com/guisehn/genetic-magic-square-finder/tree/pt-br)

Project for Artificial Intelligence module (2016/1) | [University of Santa Cruz do Sul (UNISC)](http://www.unisc.br)

Authors: Guilherme Sehn, Gabriel Bittencourt, Mateus Leonhardt

This app finds [magic squares](https://en.wikipedia.org/wiki/Magic_square) using a [genetic algorithm](https://en.wikipedia.org/wiki/Genetic_algorithm). A magic square is an arrangement of distinct numbers in arithmetical progression in a square grid, where the numbers in each row, and in each column, and the numbers in the main and secondary diagonals, all add up to the same number.

![Magic square](https://wikimedia.org/api/rest_v1/media/math/render/svg/3bc23e727d4029de3d46c2b70b8eafd4fa718b70)

## Algorithm parameters
![User interface](https://cloud.githubusercontent.com/assets/830208/16735832/e6e83336-4760-11e6-8c72-30d0a8b4e395.png)

### Population

The available parameters are:

1. **Square size:** size of the magic square

2. **Population size:** amount of individuals in the population. This number is fixed for all generations of the genetic algorithm.

3. **Allow identical individuals:** the algorithm tends to generate identical individuals while advancing, making it difficult to converge to new magic squares. If this option is checked, the algorithm will not allow duplicate individuals in the population. In this case, if a crossover between two parents end up resulting in an individual which is already in the next generation, this individual will be discarded and two different parents will be selected within the mating pool to cross over and generate another individual. It is recommended to keep this option active for faster convergence.

### Elitism

1. **Elite size:** amount of individuals with the highest fitness scores among the population that will be transferred to the next generation automatically. The value of this option must be less than the size of the population, otherwise the algorithm won't be able to generate new individuals. If this option is set to `0`, there won't be elitism behavior on the execution of the genetic algorithm. Based on the tests performed so far, it was noticed that a big elite accelerates the convergence for this problem.

2. **Elite death period**: a value that indicates the maximum amount of generations that the elite can survive without finding new magic squares. If this option is set to `0` this option will be disabled. If a positive number *N* is set, after *N* generations without finding new magic squares the individuals belonging to the elite will not be considered in the *mating pool* creation and will not be transferred to the next generation. After that, a new elite will be formed through the crossover of the individuals remaining in the population. Keeping this option active generally improves the performance of the genetic algorithm for this problem, specially for big square sizes.

### Crossover

1. **Minimum point and maximum point:** the algorithm uses a one-point crossover along with some other techniques. The crossover point is selected randomly and these fields can be used to define the range allowed for the random selector, which can be in between `0` (inclusive) and `N` (exclusive) where `N` is the size of the magic square array (e.g. 9 for a 3x3 square). More information can be seen in the [crossover function](#crossover-function) section.

2. **Mutation chance:** percentage of new individuals generated by crossover which will suffer [mutation](#mutation-behavior).

### Output

1. **Show generation details:** if checked, the system will present full information about the origin of the individuals in the log section (first parent, second parent, whether it belongs to elite or not, and mutation points). For better performance, it is recommended to keep it unchecked.

2. **Output full population log to file:** if checked, it will create a new file containing the full generation log in the `output` folder on the same directory where the app is located. It is recommended to keep it deactivated for better performance and to avoid high usage of disk space. **Important: the output files can become large quickly because of the amount of data generated. Running the algorithm for 4x4 square might create files larger than 1 GB.**

3. **Clear log periodically:** If it's unchecked, the app will attempt to show all the genetic algorithm log into the screen. It's recommended to keep this option **activated** otherwise it is very likely that a memory overflow will happen after some time of execution.

## Algorithm definitions

### Individual representation

Each individual is represented using a plain array with the lines of the matrix in sequence. Therefore an array `[2, 7, 6, 9, 5, 1, 4, 3, 8]` represents the square:

```
2 7 6
9 5 1
4 3 8
```

### Fitness function

1. The *magic number* is calculated using the formula `(L+(L^3))/2` where `L` is the square size. This is the expected value for the sum of each line, column and diagonal of a square for it to be considered a magic square. We'll call this value `M` from now on.

2. The sum `S` for each line, column and diagonal is calculated. Also, for each line, it's calculated `N = |M-S|` (*magic number - sum*)

3. All calculated `N`s summed up result in the fitness value which is better the closer to 0. A magic square will have fitness value `0`, and the fitness value increases proportionally to the "distance" required for it to become a magic square.

#### Example

```
Size: 3x3

Square:
1 2 3
4 5 6
7 8 9

L = 3
M = (3+(3^3))/2 = 15

Line 1:
S = 1+2+3 = 6
N = |M-S| = |15-6| = 9

Line 2:
S = 4+5+6 = 15
N = |M-S| = |15-15| = 0

Line 3:
S = 7+8+9 = 24
N = |M-S| = |15-24| = 9

Column 1:
S = 1+4+7 = 12
N = |M-S| = |15-12| = 3

Column 2:
S = 2+5+8 = 15
N = |M-S| = |15-15| = 0

Column 3:
S = 3+6+9 = 18
N = |M-S| = |15-18| = 3

Diagonal 1:
S = 1+5+9 = 15
N = |M-S| = |15-15| = 0

Diagonal 2:
S = 3+5+7 = 15
N = |M-S| = |15-15| = 0

Fitness = sum of all N values = 9+0+9+3+0+3+0+0 = 24
```

### Selection for crossover
The individuals are selected for crossover through [tournment selection](https://en.wikipedia.org/wiki/Tournament_selection). The size of the mating pool is equal to half of the population size.

Therefore if the size of the population is 200, the size of the mating pool will be 100. For each new generation, a new empty mating pool is created. Two new individuals are selected randomly from the population and the one with the highess fitness score is added to the mating pool. In case of tie, one of the individuals is added to the mating pool. This procedure is repeated until the mating pool gets to the expected size.

### Crossover function
All the numbers of a magic square must be unique, therefore having repeated numbers in the array that represents a magic square is not allowed. Simple crossover methods such as one-point, N-point, cut-and-splice and some others are not feasible for this problem since they generate individuals with repeated numbers very often. Because of this, it was necessary to use a more sophisticated crossover method.

The crossover function used by the program was proposed on the paper [*Genetic Algorithm Solution of the TSP Avoiding Special Crossover and Mutation*](http://www.ceng.metu.edu.tr/~ucoluk/research/publications/tspnew.pdf) written by Göktürk Üçoluk. The paper proposes a crossover method for the [*Travelling Salesman Problem*](https://en.wikipedia.org/wiki/Travelling_salesman_problem), which can also be used for the magic square problem since both problems use permutation representation, in which genes cannot repeat.

Each crossover uses two individuals as parents. For each parent the inversion sequence array is calculated, which is an alternative and reversible representation of the same individual. Being reversible means that we can recalculate the original permutation representation having the inversion sequence as the input.

#### Inversion sequence creation example

For calculating the inversion sequence of `[2, 7, 6, 9, 5, 1, 4, 3, 8]`, an empty array of the same size is created and the following steps are performed:

1. On the 1st iteration, we search for the value `1` in the original array and count the amount of elements on its left which are bigger than it. In this case, there are five: 2, 7, 6, 9 and 5. Therefore, the value of the 1st position of the inversion sequence will be `5`. On this iteration, `inv = [5, _, _, _, _, _, _, _, _]`.

2. On the 2nd iteration, we search for the value `2` in the original array and count the amount of elements on its left which are bigger than it. There are no numbers on its left, so the value of the 2nd position of the inversion sequence will be `0`. On this iteration, `inv = [5, 0, _, _, _, _, _, _, _]`.

3. On the 3rd iteration, we search for the value `3` in the original array and count the amount of elements on its left which are bigger than it. There are five numbers bigger than it on the left: 7, 6, 9, 5 and 4. Therefore the value on the 3rd position is `5`. On this iteration, `inv = [5, 0, 5, _, _, _, _, _, _]`.

Those steps are repeated until the inversion sequence array is filled. In the end, the result is `[5, 0, 5, 4, 3, 1, 0, 1, 0]`.

This representation of the square can now be crossed over without generating an invalid individual.

#### Generating the children arrays

Having the inversion sequence of the two parents, a simple one-point crossover method is used to generate two children. The crossover point is generated randomly between the limits `A` and `B` (inclusive) specified by the user on the application interface.

If we have the following inputs:

- First parent = `[5, 0, 5, 4, 3, 1, 0, 1, 0]`
- Second parent = `[1, 6, 6, 0, 0, 0, 2, 1, 0]`
- Crossover point = `5`

The following children will be generated from it:

- First child = `[5, 0, 5, 4, 3, 1, 2, 1, 0]`
- Second child = `[1, 6, 6, 0, 0, 0, 0, 1, 0]`

The first one contains the first six elements of the first parent (elements from index `0` to `5`) and the rest of the second parent, and the second one contains the first six elements of the second parent and the rest of the first parent.

These new individuals are represented as inversion sequences, and now we need to transform them back to the permutation representation.

#### Transforming back to permutation representation (example)

First, let's calculate an intermediate array called `pos` with the same size as the array that represents the square and the inversion sequence.

```
Start of algorithm:
inv = [5, 0, 5, 4, 3, 1, 2, 1, 0]
pos = [_, _, _, _, _, _, _, _, _] (all positions of "pos" are empty)

1st iteration (i = 8):
Copy inv[8] to pos[8]: pos = [_, _, _, _, _, _, _, _, 0]
There are no elements to the right of pos[8], so the 1st iteration is done.

2nd iteration (i = 7):
Copy inv[7] to pos[7]: pos = [_, _, _, _, _, _, _, 1, 0]
Search for elements to the right of pos[7] which are greather or equal to its value (1).
No elements found, so the 2nd iteration is done.

3rd iteration (i = 6):
Copy inv[6] to pos[6]: pos = [_, _, _, _, _, _, 2, 1, 0]
Search for elements to the right of pos[6] which are greather or equal to its value (2).
No elements found, so the 3rd iteration is done.

4th iteration (i = 5):
Copy inv[5] to pos[5]: pos = [_, _, _, _, _, 1, 2, 1, 0]
Search for elements to the right of pos[5] which are greather or equal to its value (1).
Two elements found: pos[6] is 2 and pos[7] is 1. Increment these values (pos[6]++ and pos[7]++).
In the end of this iteration, pos = [_, _, _, _, _, 1, 3, 2, 0]

5th iteração (i = 4):
Copy inv[4] to pos[4]: pos = [_, _, _, _, 3, 1, 3, 2, 0]
pos[6] >= pos[4], so pos[6]++
In the end of this iteration, pos = [_, _, _, _, 3, 1, 4, 2, 0]

6th iteration (i = 3):
Copy inv[3] to pos[3]: pos = [_, _, _, 4, 3, 1, 4, 2, 0]
pos[6] >= pos[3], so pos[6]++
In the end of this iteration, pos = [_, _, _, 4, 3, 1, 5, 2, 0]

7th iteration (i = 2):
Copy inv[2] to pos[2]: pos = [_, _, 5, 4, 3, 1, 5, 2, 0]
pos[6] >= pos[2], so pos[6]++
In the end of this iteration, pos = [_, _, 5, 4, 3, 1, 6, 2, 0]

8th iteration (i = 1):
Copy inv[1] to pos[1]: pos = [_, 0, 5, 4, 3, 1, 6, 2, 0]
All items to the right of pos[1] are greather or equal than it, so all of them will be incremented.
In the end of this iteration, pos = [_, 0, 6, 5, 4, 2, 7, 3, 1]

9th iteration (i = 0):
Copy inv[0] to pos[0]: pos = [5, 0, 6, 5, 4, 2, 7, 3, 1]
pos[2], pos[3] and pos[6] >= pos[0], so pos[2]++, pos[3]++, pos[6]++
In the end of this iteration, pos = [5, 0, 7, 6, 4, 2, 8, 3, 1]

The loop ended and "pos" is ready, now let's use "pos" to mount the final representation.

pos = [5, 0, 7, 6, 4, 2, 8, 3, 1]
square = [_, _, _, _, _, _, _, _, _] // all positions of "square" are empty

Iteration 1: if pos[0] is 5 then square[5] = 1, so square is [_, _, _, _, _, 1, _, _, _]
Iteration 2: if pos[1] is 0 then square[0] = 2, so square is [2, _, _, _, _, 1, _, _, _]
Iteration 3: if pos[2] is 7 then square[7] = 3, so square is [2, _, _, _, _, 1, _, 3, _]
Iteration 4: if pos[3] is 6 then square[6] = 4, so square is [2, _, _, _, _, 1, 4, 3, _]
Iteration 5: if pos[4] is 4 then square[4] = 5, so square is [2, _, _, _, 5, 1, 4, 3, _]
Iteration 6: if pos[5] is 2 then square[2] = 6, so square is [2, _, 6, _, 5, 1, 4, 3, _]
Iteration 7: if pos[6] is 8 then square[8] = 7, so square is [2, _, 6, _, 5, 1, 4, 3, 7]
Iteration 8: if pos[7] is 3 then square[3] = 8, so square is [2, _, 6, 8, 5, 1, 4, 3, 7]
Iteration 9: if pos[8] is 1 then square[1] = 9, so square is [2, 9, 6, 8, 5, 1, 4, 3, 7]

(iteration X: if pos[X-1] is Y then square[Y] = X)
```

In the end of the execution, `square` (first children) is `[2, 9, 6, 8, 5, 1, 4, 3, 7]`. The same calculation happens for the second child.

### Mutation behavior
Mutation is random and may occur or not after a crossover. When a mutation occurs, two random positions from the array are swapped. Example:

- Original individual: `[1, 2, 3, 4, 5, 6, 7, 8, 9]`
- Mutation positions (random, array starts at the position `0`): `[3, 6]`
- Individual after mutation: `[1, 2, 3, 7, 5, 6, 4, 8, 9]`

# Results and benchmarks

Some execution logs can be found on the folder [misc/execution-times](https://github.com/guisehn/genetic-magic-square-finder/tree/master/misc/execution-times) of this repository. They contain the parameters used for running the tests, the magic squares found along with all their crossover information and the time consumed by the program to find them.

## Comparison with brute force

Another version of this app using only brute force through permutation can be found at https://github.com/guisehn/magic-square-finder

It takes several hours to find magic squares bigger than 3x3. More details can be found on the repo page.
