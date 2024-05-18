package GraphRepresentations;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GraphUsingAdjacencyList implements GraphInterface {
    private final List<List<Integer>> adjacencyList;
    private int numVertices;

    public int getNumVertices() {
        return numVertices;
    }

    public GraphUsingAdjacencyList(int numVertices) {
        this.numVertices = numVertices;
        adjacencyList = new ArrayList<>(numVertices);
        for (int i = 0; i < numVertices; i++) {
            adjacencyList.add(new LinkedList<>());
        }
    }

    public void addEdge(Edge edge) {
        if (isValidVertex(edge.getSrc()) && isValidVertex(edge.getDest())) {
            adjacencyList.get(edge.getSrc()).add(edge.getDest());
            adjacencyList.get(edge.getDest()).add(edge.getSrc());
        } else {
            System.out.println("Invalid vertex index");
        }
    }

    public void removeEdge(int source, int destination) {
        if (isValidVertex(source) && isValidVertex(destination)) {
            adjacencyList.get(source).remove(Integer.valueOf(destination));
            adjacencyList.get(destination).remove(Integer.valueOf(source));
        } else {
            System.out.println("Invalid vertex index");
        }
    }

    public void addVertex() {
        numVertices++;
        adjacencyList.add(new LinkedList<>());
    }

    public void removeVertex(int vertex) {
        if (isValidVertex(vertex)) {
            numVertices--;
            adjacencyList.remove(vertex);

            // Remove references to the removed vertex in other vertices' lists
            for (List<Integer> list : adjacencyList) {
                list.remove(Integer.valueOf(vertex));
            }
        } else {
            System.out.println("Invalid vertex index");
        }
    }

    public boolean containsVertex(int vertex) {
        return isValidVertex(vertex);
    }

    public boolean containsEdge(int source, int destination) {
        if (isValidVertex(source) && isValidVertex(destination)) {
            return adjacencyList.get(source).contains(destination);
        } else {
            System.out.println("Invalid vertex index");
            return false;
        }
    }
    private boolean isValidVertex(int vertex) {
        return vertex >= 0 && vertex < numVertices;
    }

    public void traverseGraph(int startVertex) {
        if (isValidVertex(startVertex)) {
            boolean[] visited = new boolean[numVertices];
            traverseGraphRecursive(startVertex, visited);
            System.out.println();
        } else {
            System.out.println("Invalid start vertex index");
        }
    }

    private void traverseGraphRecursive(int currentVertex, boolean[] visited) {
        visited[currentVertex] = true;
        System.out.print(currentVertex + " ");

        for (int neighbor : adjacencyList.get(currentVertex)) {
            if (!visited[neighbor]) {
                traverseGraphRecursive(neighbor, visited);
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numVertices; i++) {
            sb.append(i).append(": ");
            for (int neighbor : adjacencyList.get(i)) {
                sb.append(neighbor).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }


}
