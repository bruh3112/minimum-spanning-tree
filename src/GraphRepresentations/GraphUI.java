package GraphRepresentations;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Comparator;
import java.util.List;

public class GraphUI extends JFrame {
    private static MinimumSpanningTree graph;
    private final JTextArea outputArea;
    private final GraphPanel graphPanel;
    private List<Edge> mstEdges;

    public GraphUI() {
        // Set up the JFrame
        setTitle("Graph UI");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create components
        JPanel controlPanel = createControlPanel();
        outputArea = new JTextArea();
        graphPanel = new GraphPanel();

        // Set up the layout using JSplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(createOutputPanel());
        splitPane.setBottomComponent(graphPanel);

        // Add the control panel to the JFrame
        add(controlPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        // Redirect System.out to JTextArea
        PrintStream printStream = new PrintStream(new CustomOutputStream(outputArea));
        System.setOut(printStream);
        System.setErr(printStream);

        // Initialize the graph
        graph = new MinimumSpanningTree(0);
    }

    private JPanel createOutputPanel() {
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        return outputPanel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton addVertexButton = new JButton("Add Vertex");
        JButton addEdgeButton = new JButton("Add Edge");
        JButton deleteEdgeButton = new JButton("Delete Edge");
        JButton runAlgorithmsButton = new JButton("Run Algorithms");
        JButton importFromTxtButton = new JButton("Import from TXT");

        addVertexButton.addActionListener(e -> {
            int result = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of vertices:"));
            graph = new MinimumSpanningTree(result);
            graphPanel.repaint();  // Repaint the graph panel
            outputArea.setText("Graph created with " + result + " vertices.\n");
        });

        addEdgeButton.addActionListener(e -> {
            int source = Integer.parseInt(JOptionPane.showInputDialog("Enter source vertex:"));
            int destination = Integer.parseInt(JOptionPane.showInputDialog("Enter destination vertex:"));
            int weight = Integer.parseInt(JOptionPane.showInputDialog("Enter weight:"));
            graph.addEdge(new Edge(source, destination, weight));
            graphPanel.repaint();  // Repaint the graph panel
            outputArea.append("Edge added: " + source + " -- " + destination + " == " + weight + "\n");
        });

        deleteEdgeButton.addActionListener(e -> {
            int source = Integer.parseInt(JOptionPane.showInputDialog("Enter source vertex: "));
            int destination = Integer.parseInt(JOptionPane.showInputDialog("Enter destination vertex:"));
            graph.removeEdge(source, destination);
            graphPanel.repaint();
            outputArea.append("Edge deleted: " + source + " -- " + destination + "\n");
        });

        importFromTxtButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                importFromTxt(filePath);
                graphPanel.repaint();
                outputArea.append("Graph imported from TXT file: " + filePath + "\n");
            }
        });

        runAlgorithmsButton.addActionListener(e -> {
            int[][] adjacencyMatrix = graph.toAdjacencyMatrix();

            // Print the adjacency matrix to outputArea
            outputArea.append("Adjacency Matrix:\n");
            for (int[] matrix : adjacencyMatrix) {
                for (int i : matrix) {
                    outputArea.append(i + "\t");
                }
                outputArea.append("\n");
            }

            // Run Prim's MST
            outputArea.append("\nPrim's Algorithm\n");
            long primStartTime = System.currentTimeMillis();
            graph.primMST(adjacencyMatrix);
            long primEndTime = System.currentTimeMillis();
            mstEdges = graph.getPrimMSTEdges();
            outputArea.append("\nTime taken by Prim's MST: " + (primEndTime - primStartTime) + " milliseconds\n");

            graphPanel.repaint();

            // Run Kruskal's MST
            outputArea.append("\nKruskal's Algorithm\n");
            long kruskalStartTime = System.currentTimeMillis();
            List<Edge> edges = graph.getAllEdges();
            edges.sort(Comparator.comparingInt(Edge::getWeight));
            graph.kruskal(graph.getNumVertices());
            long kruskalEndTime = System.currentTimeMillis();
            outputArea.append("Time taken by Kruskal's MST: " + (kruskalEndTime - kruskalStartTime) + " milliseconds\n");
        });

        panel.add(addVertexButton);
        panel.add(addEdgeButton);
        panel.add(deleteEdgeButton);
        panel.add(importFromTxtButton);
        panel.add(runAlgorithmsButton);

        return panel;
    }

    private class GraphPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawGraph(g);
        }

        private void drawGraph(Graphics g) {
            int[][] adjacencyMatrix = graph.toAdjacencyMatrix();
            int numVertices = graph.getNumVertices();

            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int radius = 150;

            // Draw vertices with different colors
            for (int i = 0; i < numVertices; i++) {
                double angle = 2 * Math.PI * i / numVertices;
                int x = (int) (centerX + radius * Math.cos(angle));
                int y = (int) (centerY + radius * Math.sin(angle));

                // Set different colors for vertices
                g.setColor(Color.BLACK); // Change the color as needed
                g.fillOval(x - 15, y - 15, 30, 30);
                g.setColor(Color.WHITE);
                g.drawString(Integer.toString(i), x, y);
            }

            // Draw edges with different colors
            for (int i = 0; i < numVertices; i++) {
                for (int j = i + 1; j < numVertices; j++) {
                    if (adjacencyMatrix[i][j] != 0) {
                        double angle1 = 2 * Math.PI * i / numVertices;
                        double angle2 = 2 * Math.PI * j / numVertices;
                        int x1 = (int) (centerX + radius * Math.cos(angle1));
                        int y1 = (int) (centerY + radius * Math.sin(angle1));
                        int x2 = (int) (centerX + radius * Math.cos(angle2));
                        int y2 = (int) (centerY + radius * Math.sin(angle2));

                        // Set different colors for edges
                        if (isEdgeInMST(i, j)) {
                            // If the edge is in the MST, draw it in red
                            g.setColor(Color.RED);
                        } else {
                            // Otherwise, draw it in black
                            g.setColor(Color.BLACK);
                        }
                        g.drawLine(x1, y1, x2, y2);
                        g.drawString(Integer.toString(adjacencyMatrix[i][j]), (x1 + x2) / 2, (y1 + y2) / 2);
                    }
                }
            }
        }
    }

    private void importFromTxt(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 3) {
                    int source = Integer.parseInt(parts[0]);
                    int destination = Integer.parseInt(parts[1]);
                    int weight = Integer.parseInt(parts[2]);
                    graph.addEdge(new Edge(source, destination, weight));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GraphUI graphUI = new GraphUI();
            graphUI.setVisible(true);
        });
    }

    private static class CustomOutputStream extends OutputStream {
        private final JTextArea textArea;

        public CustomOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) {
            textArea.append(String.valueOf((char) b));
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }

    private boolean isEdgeInMST(int source, int destination) {
        if (mstEdges == null) {
            return false;
        }

        for (Edge edge : mstEdges) {
            if ((edge.getSrc() == source && edge.getDest() == destination) ||
                    (edge.getSrc() == destination && edge.getDest() == source)) {
                return true;
            }
        }
        return false;
    }
}
