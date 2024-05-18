package GraphRepresentations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MinimumSpanningTree extends GraphUsingAdjacencyMatrix{
    public MinimumSpanningTree(int numVertices) {
        super(numVertices);
    }

    public List<Edge> findMinimumSpanningTreeEdges(Algorithm algorithm) {
        List<Edge> edges = getAllEdges();
        edges.sort(Comparator.comparingInt(Edge::getWeight));

        int j = 0;
        int noOfEdges = 0;

        Subset[] subsets = new Subset[getNumVertices()];
        Edge[] results = new Edge[getNumVertices()];

        for (int i = 0; i < getNumVertices(); i++) {
            subsets[i] = new Subset(i, 0);
        }

        while (j < edges.size() && noOfEdges < getNumVertices() - 1) {
            Edge nextEdge = edges.get(j);
            int x = findRoot(subsets, nextEdge.getSrc());
            int y = findRoot(subsets, nextEdge.getDest());

            if (x != y) {
                results[noOfEdges] = nextEdge;
                union(subsets, x, y);
                noOfEdges++;
            }

            j++;
        }

        return new ArrayList<>(Arrays.asList(results).subList(0, noOfEdges));
    }


    private int minKey(int[] key, Boolean[] mstSet) {
        // Initialize min value
        int min = Integer.MAX_VALUE, min_index = -1;

        for (int v = 0; v < getNumVertices(); v++)
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                min_index = v;
            }

        return min_index;
    }

    // A utility function to print the constructed MST
    // stored in parent[]
    private void printMST(int[] parent, int[][] graph) {
        System.out.println("Edge \tWeight");
        for (int i = 1; i < getNumVertices(); i++)
            System.out.println(parent[i] + " - " + i + "\t"
                    + graph[i][parent[i]]);
    }

    // Function to construct and print MST for a graph
    // represented using adjacency matrix representation
    public void primMST(int[][] graph) {
        // To represent set of vertices included in MST
        Boolean[] mstSet = new Boolean[getNumVertices()];



        // Key values used to pick minimum weight edge in
        // cut
        int[] key = new int[getNumVertices()];

        // Initialize all keys as INFINITE
        for (int i = 0; i < getNumVertices(); i++) {
            key[i] = Integer.MAX_VALUE;
            mstSet[i] = false;
        }

        // Always include first 1st vertex in MST.
        // Make key 0 so that this vertex is
        // picked as first vertex
        key[0] = 0;

        // Array to store constructed MST
        int[] parent = new int[getNumVertices()];
        // First node is always root of MST
        parent[0] = -1;

        // The MST will have V vertices
        for (int count = 0; count < getNumVertices() - 1; count++) {

            // Pick the minimum key vertex from the set of
            // vertices not yet included in MST
            int u = minKey(key, mstSet);

            // Check if minKey returned a valid index
            if (u != -1) {
                // Add the picked vertex to the MST Set
                mstSet[u] = true;

                // Update key value and parent index of the
                // adjacent vertices of the picked vertex.
                // Consider only those vertices which are not
                // yet included in MST
                for (int v = 0; v < getNumVertices(); v++) {
                    if (graph[u][v] != 0 && !mstSet[v]
                            && graph[u][v] < key[v]) {
                        parent[v] = u;
                        key[v] = graph[u][v];
                    }
                }
            }
        }

        // Print the constructed MST
        printMST(parent, graph);
    }

    public List<Edge> getPrimMSTEdges() {
        return findMinimumSpanningTreeEdges(Algorithm.PRIM);
    }

    public void kruskal(int V) {
        List<Edge> edges = getAllEdges();
        edges.sort(Comparator.comparingInt(Edge::getWeight));

        int j = 0;
        int noOfEdges = 0;

        // Allocate memory for creating V subsets
        Subset[] subsets = new Subset[V];

        // Allocate memory for results
        Edge[] results = new Edge[V];

        // Create V subsets with single elements
        for (int i = 0; i < V; i++) {
            subsets[i] = new Subset(i, 0);
        }

        // Number of edges to be taken is equal to V-1
        while (noOfEdges < V - 1 && j < edges.size()) {
            // Pick the smallest edge. And increment
            // the index for next iteration
            Edge nextEdge = edges.get(j);
            int x = findRoot(subsets, nextEdge.getSrc());
            int y = findRoot(subsets, nextEdge.getDest());

            // If including this edge doesn't cause a cycle,
            // include it in the result and increment the index
            // of the result for the next edge
            if (x != y) {
                results[noOfEdges] = nextEdge;
                union(subsets, x, y);
                noOfEdges++;
            }

            j++;
        }

        // Print the contents of result[] to display the
        // built MST
        System.out.println("Following are the edges of the constructed MST:");
        int minCost = 0;
        for (int i = 0; i < noOfEdges; i++) {
            System.out.println(results[i].getSrc() + " -- "
                    + results[i].getDest() + " == "
                    + results[i].getWeight());
            minCost += results[i].getWeight();
        }
        System.out.println("Total cost of MST: " + minCost);
    }


    public List<Edge> getKruskalMSTEdges() {
        return findMinimumSpanningTreeEdges(Algorithm.KRUSKAL);
    }

    // Function to unite two disjoint sets
    private static void union(Subset[] subsets, int x, int y) {
        int rootX = findRoot(subsets, x);
        int rootY = findRoot(subsets, y);

        if (subsets[rootY].rank < subsets[rootX].rank) {
            subsets[rootY].parent = rootX;
        }
        else if (subsets[rootX].rank < subsets[rootY].rank) {
            subsets[rootX].parent = rootY;
        }
        else {
            subsets[rootY].parent = rootX;
            subsets[rootX].rank++;
        }
    }

    // Function to find parent of a set
    private static int findRoot(Subset[] subsets, int i) {
        if (subsets[i].parent == i) {
            return subsets[i].parent;
        }
        subsets[i].parent = findRoot(subsets, subsets[i].parent);
        return subsets[i].parent;
    }

    private enum Algorithm {
        PRIM, KRUSKAL
    }
}
