# snake-pit

snake-pit is a student project written in Clojure to demonstrate application of genetic programming to the snake game. Genetic programming is a evolutionary computation technique inspired by nature which enables a population of computer programs to evolve in order to solve some problems. In this case, the individual program is a tree structure that represents a control routine handling the snake's movement on the game board. The goal is to evolve a program from initial random population that enables the snake to eat as many pieces of food and survive as long as possible.

![Genetic programming algorithm](https://github.com/somi92/snake-pit/blob/master/resources/flowchart.jpg?raw=true "Genetic programming flowchart") 

Image 1 - Genetic programming flowchart (source http://geneticprogramming.us/What_is_Genetic_Programming.html)


To find out more about GP check out this book [A Field Guide to Genetic Programming](http://www.gp-field-guide.org.uk/).

The project uses [fungp - a genetic programming library for Clojure] (https://github.com/vollmerm/fungp) by [Mike Vollmer](https://github.com/vollmerm).

The project is inspired by this article: [Application of Genetic Programming to the Snake Game](http://www.gamedev.net/page/resources/_/technical/artificial-intelligence/application-of-genetic-programming-to-the-snake-r1175).

## Description

In this implementation the game board size is 20 squares wide and 11 squares high.The snake has 9 body squares and starts at position (1,11)-(9,11) moving to the right. Food pieces (apples) pop up at random positions. By eating apples, snake grows one body square at a time. The maximum number of apples to eat is 211, at which point the snake fill the whole game board. In order to survive the snake needs to avoid hitting the wall or its own body.

The snakes has sensor functions that enable it to sense its environment. Each sensor function has two arguments and executes its first argument if the condition is true, else it executes its second argument. The exception is Clojure's `do` function, which executes both arguments.

* Initial function set (basic sensor functions)
  - `if-food-ahead` Check if an apple is in line with the snake's current direction.
  - `if-danger-ahead` Check if position ahead of current snake's direction is occupied by a wall or snake segment.
  - `if-danger-right` Check if position to the right of current snake's direction is occupied by a wall or snake segment.
  - `if-danger-left` Check if position to the left of current snake's direction is occupied by a wall or snake segment.
  - `do` Executes both arguments.
  
* Full function set (additional sensor functions)
  - `if-danger-two-ahead` Check if position two steps ahead of current snake's direction is occupied by a wall or snake segment.
  - `if-food-up` Check if the current apple on the board is closer to the top of the board than the snake's head.
  - `if-food-right` Check if the current apple on the board is further to the right of the board than the snake's head.
  - `if-moving-right` Check if the snake's current direction is right.
  - `if-moving-left` Check if the snake's current direction is left.
  - `if-moving-up` Check if the snake's current direction is up.
  - `if-moving-down` Check if the snake's current direction is down.

In order to move, the snakes uses three terminal functions for each direction `turn-right`, `turn-left`, `move-forward`.

When the GP starts, a population of random programs is created. A program which represents the snake's control routine is a tree structure consisting of nested sensor function calls down to the terminals. For example this is one small randomly created program:
```
(if-food-ahead
 (move-forward)
 (if-food-right
  (move-forward)
  (turn-left)))
```

Then, the GP runs each program and calculates its fitness. One run consists of GP executing a program (control routine) before each snake move until the death of the snake. The fitness is calculated as `maximum number of apples (211) - score (number of apples eaten)`, so the lower the number the better performace of a program. To eliminate the possibility of a program getting a better fitness due to lucky apple placement in a run, each program has four runs so the fitness is `(211*4)-(sum of scores in all four runs)`. Each program has to be able to collect the apple in maximum 300 square moves or it is killed. This prevents programs to get stuck in a loop pattern which is ineffective in apple collecting. If the program hasn't reached any apples it is penalized according to the distance from the apple. The best programs are then selected in tournament selection and crossed with each other (they exchange parts of their tree structure) in order to produce new generation of hopefully better programs. Some programs are selected to be randomly mutated. This process repeats until the GP is stopped manualy or it runs the defined number of generations (iterations*migrations) or some program reaches the fitness below 50. 

To find out more about GP functions, terminals, tournament selection, mutation and other GP stuff, read the first chapter of the book referenced above and refer to the above article.  

## Installation

This software is intended to be used as a library and all you need to do is to include the jar file into the classpath of your project. The jar file can be found in the releases section of this respository.

## Usage

To use the software, call the `run_snakes_pit` function and pass it a map with the following keyword parameters:

* iterations : number of iterations between migrations
* migrations : number of migrations
* num_islands : number of islands
* population_size : size of the populations
* tournament_size : size of the tournaments (default 5)
* mutation_probability : probability of mutation (default 0.2)
* max_depth : maximum depth of trees
* functions : "full" for full function set, "init" for initial function set ("full" is default)

The GP will report the best program at the end of each migration along with its fitness.

To use the library from Java, include it in your project and create a Java bean object containing attributes named the same as the above keywords with appropriate getters and setters. You can also implement a method with the following signature `public void onResult(String message)` in your Java object and the GP will call it passing in the report at the end of each migration.

If you wish to change the reporting functionality or any other part of the project feel free to do so. Refer to Mike's project documentation for help.

## Example run

The following example run uses small parameter values for convenience of displaying reports here and will not produce very efficient programs.

```
(use 'snake-pit.core)

(def gp-options {:iterations 2 :migrations 2 :num_islands 4 
                 :tournament_size 5 :population_size 500 
                 :max_depth 3 :mutation_probability 0.3 
                 :functions "init"})

(run_snakes_pit gp-options)
```

Output:

```
Snake game

(do
 (if-danger-ahead
  (if-danger-left
   (turn-right)
   (turn-right))
  (if-danger-right
   (turn-left)
   (turn-right)))
 (if-danger-left
  (if-danger-left
   (move-forward)
   (turn-right))
  (if-danger-ahead
   (turn-left)
   (turn-left))))
	
Error:	841

(do
 (if-danger-ahead
  (if-danger-left
   (turn-right)
   (turn-right))
  (if-danger-right
   (turn-left)
   (turn-right)))
 (if-danger-left
  (if-danger-left
   (move-forward)
   (turn-right))
  (if-danger-ahead
   (turn-left)
   (turn-left))))
	
Error:	836

Done!

(do
 (if-danger-ahead
  (if-danger-left
   (turn-right)
   (turn-right))
  (if-danger-right
   (turn-left)
   (turn-right)))
 (if-danger-left
  (if-danger-left
   (move-forward)
   (turn-right))
  (if-danger-ahead
   (turn-left)
   (turn-left))))

Error:	829
```



## Results

The program produced by GP follows a specific pattern of movement in order maximize its score. For example it can evolve a zig-zag pattern in which it moves in a constant zig-zig schema diagonally across the board turning when facing the wall or body segment. Another possible pattern is "wall-follower" which follows the wall and jumps inside the board from specific points to collect the apples. This pattern is usually better than zig-zag because the snake doesn't charge at the apple directly but it learns how to create more space to maneuver which leads to less chance of hiting itself when it grows to a bigger size. Generally speaking, more advanced programs tend to pay much less attention to the apple and focus more on patterns that enable them to cover more squares of the game board.

Initial tests produced the programs that score maximum of 40-70 with runs lasting about 4-5 hours on a single machine with parameters approximately: 10-20 iterations, 5-15 migrations, 4 islands, 7-10 tournament size, 3-5 max depth. The big limitation is the that the fungp library allows maximum of 3500 individuals and implementation described in the article above starts with a 10000 and farms out runs to seperate computers, hence achieves the better results. The project requires further experimenting with different parameter setups and machines with more resources available.

## Tests

Unit test are written for all functions related to GP function sets. 

## Related projects

This project doesn't contain any GUI and results are presented in string format as tree-like composition of functions. In order to see the program in action checkout [clojure-snake](https://github.com/somi92/clojure-snake) project. It implements the snake game GUI in Clojure and Swing and it can use the output of snake-pit as well as enabling manual play.

Also, for full convenience there is the [snake-pit-ui](https://github.com/somi92/snake-pit-ui) project written in Java which wraps both snake-pit and clojure-snake with a nice UI with all the options available. It enables easy and fast experimenting with GP settings, testing the results as well as manual play.   

## Contact

If you have some comments, suggestions or noticed some bugs and problems feel free to contact me and contribute.

Developed by Milos Stojanovic 

email: stojanovicmilos31@gmail.com

## License

Copyright © 2015

Distributed under the Eclipse Public License. The copy of the license is available in the repository.
