package wordshistogram;

import java.util.ArrayList;

/**
 *
 * @author pavlat
 */
public class EdgesCollector {

    private final ArrayList<Edge> edges;

    public EdgesCollector() {
        this.edges = new ArrayList<>();
    }

    public void addEdge(OneFile file1, OneFile file2, ArrayList<String> same) {
        System.out.println("Adding edge:");
        System.out.println(" File 1: " + file1.getFile().getName());
        System.out.println(" File 2: " + file2.getFile().getName());
        this.edges.add(new Edge(file1, file2, same));
    }

    public Edge getEdge(int i) {
        return edges.get(i);
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

}
