Six Degrees of Kevin Bacon (Degrees of Separation) Game
=============

Authors: Irene Feng and Orestis Lykouropoulos

##Overview
Uses BFS on implementation of Graphs in Java to find the shortest relation from any specified actors to a user-specified root actor.

## TO COMPILE ON UNIX: 
 ``$ javac -cp net-datastructures-5-0.jar BaconGame.java ListHoldingClass.java ListComparator.java AdjacencyListGraphMap.java DirectedAdjListMap.java ActorSpelling.java``

 ``$ java -cp .:net-datastructures-5-0.jar BaconGame``

##DEPENDENCIES:
 - net-datastructures-5-0.jar 
 - ListHoldingClass.java //for statistics
 - ListComparator.java //for statistics
 - AdjacencyListGraphMap.java //Graph Implementation
 - DirectedAdjListMap.java //for BFS tree
 - ActorSpelling.java //for mispellings

## INPUTS: 
 -	First, root actor. 
 - 	Then, actor to determine path to root actor.
 	- Game will keep asking for more actors to map out path to root actor until return key is pressed. 

## SAMPLE OUTPUT:

```Pick an actor as the root of the BFS tree:
Kevin Bacon
Enter an actor to show their path to Kevin Bacon
Or, to quit the program and see some amazing, surprising, astonishing stunning and mind-blowing statistics about the graph with root actor Kevin Bacon, press the return key.
Woody Harrelson
Woody Harrelson's number is 2
Woody Harrelson appeared in Cowboy Way, The (1994) with Kiefer Sutherland
Kiefer Sutherland appeared in Flatliners (1990) with Kevin Bacon
Enter an actor to show their path to Kevin Bacon

The actors with the largest distance from Kevin Bacon are [Nita Naldi: 8, John F. Hamilton: 8, Bernhard Goetzke: 8, Sara Allgood: 8, Charles Paton: 8, John Longden: 8, Jobyna Ralston: 8, Clara Bow: 8, Richard Arlen: 8]
The average distance from Kevin Bacon is 3.281024819855885
The median distance from Kevin Bacon is 3.0
The standard deviation from Kevin Bacon is 0.9199448911756116
```

## Another Sample Output: 

```Pick an actor as the root of the BFS tree:
Robert De Niro
Enter an actor to show their path to Robert De Niro
Or, to quit the program and see some amazing, surprising, astonishing stunning and mind-blowing statistics about the graph with root actor Robert De Niro, press the return key.

The actors with the largest distance from Robert De Niro are [Nita Naldi: 7, John F. Hamilton: 7, Bernhard Goetzke: 7, Sara Allgood: 7, Charles Paton: 7, John Longden: 7, Jobyna Ralston: 7, Clara Bow: 7, Richard Arlen: 7]
The average distance from Robert De Niro is 3.1134240725914064
The median distance from Robert De Niro is 3.0
The standard deviation from Robert De Niro is 0.8460010910452785
```

## Boundary Cases: 
- Asks for a new actor if an actor cannot be found
- This version of the bacon game accounts for mispelled actors. However, this can be very slow depending on the edit distance from any one actor, so we have only implemented it only for the root actor so far.