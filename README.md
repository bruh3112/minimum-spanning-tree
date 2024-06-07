# MINIMUM SPANNING TREE PROJECT

## Introduction
This project implements Kruskal's and Prim's algorithms to find the Minimum Spanning Tree (MST), aimed at applications in building a cost-effective information network. 
The project includes a backend written in Java and a user interface written in Java Swing.

## Project Structure
- **src/GraphRepresentations**: Contains the main source code of the project
  - **backend**: Contains classes and algorithms related to finding the MST
    - `GraphUsingAdjacencyList.java`: Class for constructing graphs using adjacency lists.
    - `GraphUsingAdjacencyMatrix.java`: Class for constructing graphs using adjacency matrices.
    - `MinimumSpanningTree.java`: Implements Prim's and Kruskal's algorithms to find the MST and compares the performance of both algorithms.
    - `GraphInterface.java`: Interface representing a graph.
    - `Edge.java`: Class representing an edge of the graph.
    - `Subset.java`: Class representing the Disjoint Set Union (DSU) data structure used in Kruskal's algorithm.
  - **frontend**: Contains classes related to the user interface using Java Swing.
    - `GraphUI.java`: User interface using Java Swing.
    - `MST EDGES`: A text file containing sample graph edge information.

## System Requirements
- Java Development Kit (JDK) 8 or higher.
- An IDE supporting Java (NetBeans, Eclipse, IntelliJ, etc.)

## USER GUIDE
After running the GraphUI.java program, the user will see a Swing interface with the following buttons:
- `ADD VERTEX`: Enter the total number of vertices in the graph.
- `ADD EDGE`: Enter the edge information including: source vertex - destination vertex - weight.
- `DELETE EDGE`: Delete an existing edge.
- `IMPORT FROM TXT`: Instead of manual input, you can import edge information from a TXT file with the format: source vertex - destination vertex - weight (the MST EDGES file in this project is an example). Note that you still need to add the total number of vertices before importing edge information from the TXT file.
- `RUN ALGORITHMS`: The program will print the MST information and redraw the graph, highlighting the MST in red.

## NOTE
There is still room for improvement in the project that will be updated in the future.
