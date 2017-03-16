package wordshistogram;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Adam
 */
public class ToVis {

    private final ArrayList<Class> classes;
    private final HashMap<OneFile, Integer> mappedFiles;

    public ToVis(ArrayList<Class> classes) {
        this.classes = classes;
        this.mappedFiles = new HashMap<>();
        System.out.println(strNodes());
        System.out.println(strEdges());
    }

    public String strNodes() {
        String s = "nodes = [\n";
        int i = 1;
        int j = 1;
        int numOfCl = classes.size();
        for (Class cl : classes) {
            s += packNode(i++, cl.getFiles().size() * 2, "Class " + cl.getLabel(), "dot");
            for (OneFile f : cl.getFiles()) {
//                s += packNode(j++ + numOfCl, f.getHist().size(), f.getFile().getName().substring(0, 5));
                if (this.mappedFiles.keySet().contains(f)) {
                    continue;
                }

                s += packNode(j++ + numOfCl, 1, "File " + f.getFile().getName().substring(0, 5) + "..", "box");
                this.mappedFiles.put(f, j++ + numOfCl);
            }
        }
        s += "];";
        return s;
    }

    public String packNode(int id, int value, String label, String shape) {
        return "{id: " + id + ", value: " + value + ", label: \'" + label + "\', shape: \'" + shape + "\'},\n";
    }

    public String strEdges() {
        String s = "edges = [\n";

        for (int i = 0; i < classes.size(); i++) {
            for (OneFile f : classes.get(i).getFiles()) {
                s += packEdge(this.mappedFiles.get(f) - 1, (i + 1),
                        classes.get(i).getHist().size(),
                        getFewHistData(classes.get(i).getHist()));
            }
        }
        s += "];";
        return s;
    }

    private String getFewHistData(ArrayList<String> hist) {
        String res = "";
        int maxItems = 8;
        if (hist.size() < maxItems) {
            for (String s : hist) {
                res += s + ", ";
            }
        } else {
            for (int i = 0; i < maxItems; i++) {
                res += hist.get(i) + ", ";
            }
            res += "..., ";
        }
        res = res.substring(0, res.length() - 2);
        return res.replace("\'", "\\\'").replace("\"", "\\\"");
    }

    public String packEdge(int from, int to, int value, String title) {
        return "{from: " + from + ", to: " + to + ", value: " + value + ", title: \'" + title + "\'},\n";
    }
}
