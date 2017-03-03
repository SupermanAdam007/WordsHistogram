package wordshistogram;

import java.util.ArrayList;

/**
 *
 * @author pavlat
 */
public class Edge {

    private final OneFile file1;
    private final OneFile file2;
    private final ArrayList<String> same;

    public Edge(OneFile file1, OneFile file2, ArrayList<String> same) {
        this.file1 = file1;
        this.file2 = file2;
        this.same = same;
    }

    @Override
    public String toString() {
        String s = "\nEdge: \n";
        s += " File 1: " + file1.getFile().getName() + "\n";
        s += " File 2: " + file2.getFile().getName() + "\n";
        s += " Number of similary strings: " + same.size() + "\n";
        return s;
    }

    public OneFile getFile1() {
        return file1;
    }

    public OneFile getFile2() {
        return file2;
    }

    public boolean containsFile(OneFile file) {
        return file.equals(file1) || file.equals(file2);
    }

    public boolean equals(Edge edge) {
        return edge.containsFile(file1) && edge.containsFile(file2);
    }
}
