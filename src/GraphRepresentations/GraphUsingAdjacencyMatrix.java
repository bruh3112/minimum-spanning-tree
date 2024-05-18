

package GraphRepresentations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphUsingAdjacencyMatrix implements GraphInterface{
    private int[][] adjacencyMatrix;
    private int numVertices;

    public int getNumVertices() {
        return numVertices;
    }

    public GraphUsingAdjacencyMatrix(int numVertices) {
        this.numVertices = numVertices;
        adjacencyMatrix = new int[numVertices][numVertices];
    }

    public void addEdge(Edge edge) {
        adjacencyMatrix[edge.getSrc()][edge.getDest()] = edge.getWeight();
        adjacencyMatrix[edge.getDest()][edge.getSrc()] = edge.getWeight();
    }

    public void removeEdge(int source, int destination) {
        if (source >= 0 && source < numVertices && destination >= 0 && destination < numVertices) {
            adjacencyMatrix[source][destination] = 0;
            adjacencyMatrix[destination][source] = 0;
        } else {
            System.out.println("Invalid vertex index");
        }
    }

    public void addVertex() {
        numVertices++;
        int[][] newAdjacencyMatrix = new int[numVertices][numVertices];
        for (int i = 0; i < numVertices - 1; i++) {
            newAdjacencyMatrix[i] = Arrays.copyOf(adjacencyMatrix[i], numVertices);
        }
        adjacencyMatrix = newAdjacencyMatrix;
    }

    public void removeVertex(int vertex) {
        if (vertex >= 0 && vertex < numVertices) {
            numVertices--;
            int[][] newAdjacencyMatrix = new int[numVertices][numVertices];
            int row = 0;
            int col;
            for (int i = 0; i < numVertices + 1; i++) {
                if (i == vertex) {
                    continue;
                }
                col = 0;
                for (int j = 0; j < numVertices + 1; j++) {
                    if (j == vertex) {
                        continue;
                    }
                    newAdjacencyMatrix[row][col] = adjacencyMatrix[i][j];
                    col++;
                }
                row++;
            }
            adjacencyMatrix = newAdjacencyMatrix;
        } else {
            System.out.println("Invalid vertex index");
        }
    }

    public boolean containsVertex(int vertex) {
        return vertex >= 0 && vertex < numVertices;
    }

    public boolean containsEdge(int source, int destination) {
        if (source >= 0 && source < numVertices && destination >= 0 && destination < numVertices) {
            return adjacencyMatrix[source][destination] == 1;
        } else {
            System.out.println("Invalid vertex index");
            return false;
        }
    }

    public List<Edge> getAllEdges() {
        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                if (adjacencyMatrix[i][j] != 0) {
                    edges.add(new Edge(i, j, adjacencyMatrix[i][j]));
                }
            }
        }

        return edges;
    }

    public void traverseGraph(int startVertex) {
        if (startVertex >= 0 && startVertex < numVertices) {
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

        for (int i = 0; i < numVertices; i++) {
            if (adjacencyMatrix[currentVertex][i] == 1 && !visited[i]) {
                traverseGraphRecursive(i, visited);
            }
        }
    }
    public int[][] toAdjacencyMatrix() {
        int[][] adjacencyMatrixCopy = new int[numVertices][numVertices];

        for (int i = 0; i < numVertices; i++) {
            System.arraycopy(adjacencyMatrix[i], 0, adjacencyMatrixCopy[i], 0, numVertices);
        }

        return adjacencyMatrixCopy;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numVertices; i++) {
            sb.append(i).append(": ");
            for (int j = 0; j < numVertices; j++) {
                sb.append(adjacencyMatrix[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
