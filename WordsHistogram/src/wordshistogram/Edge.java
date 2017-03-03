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
}
