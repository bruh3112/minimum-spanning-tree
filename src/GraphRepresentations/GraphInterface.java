package GraphRepresentations;

public interface GraphInterface {
    public int getNumVertices();
    public void addEdge(Edge edge);
    public void removeEdge(int source, int destination);
    public void addVertex();
    public void removeVertex(int vertex);
    public boolean containsVertex(int vertex);
    public boolean containsEdge(int source, int destination);
    public void traverseGraph(int startVertex);
    public String toString();
}

