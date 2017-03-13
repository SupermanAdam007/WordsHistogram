package wordshistogram;

import java.util.ArrayList;

/**
 *
 * @author Adam
 */
public class ToVis {

    private ArrayList<OneFile> files;

    public ToVis(ArrayList<OneFile> files) {
        this.files = files;
        System.out.println(printNodes());
    }

    public String printNodes() {
        String s = "nodes = [\n";
        int i = 1;
        for (OneFile file : files) {
            s += "{id: " + i++
                    + ", value: " + file.getHist().size()
                    + ", label: \'" + file.getFile().getName().substring(0, 5) + "..\'"
                    + "},\n";
        }
        s += "];";
        return s;
    }
}
