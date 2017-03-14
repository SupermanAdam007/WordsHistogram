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

    public ArrayList<Edge> getEdgesOfFile(OneFile f) {
        ArrayList<Edge> res = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.containsFile(f)) {
                res.add(edge);
            }
        }
        return res;
    }

    public Edge getEdge(OneFile f1, OneFile f2) {
        if (f1.equals(f2)) {
            return null;
        }
        for (Edge edge : edges) {
            if (edge.containsFile(f1) && edge.containsFile(f2)) {
                return edge;
            }
        }
        return null;
    }

}
