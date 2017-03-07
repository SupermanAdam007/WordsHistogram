package wordshistogram;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import static wordshistogram.WordsHistogram.printArrayList;

/**
 *
 * @author Adam
 */
public class ComparingManager {

    private final Set<OneFile> myFiles;
    private final ArrayList<String[]> pairs;
    private final EdgesCollector edges;

    public ComparingManager() {
        myFiles = new HashSet<>();
        pairs = new ArrayList<>();
        edges = new EdgesCollector();
    }

    public void add(File f) {
        myFiles.add(new OneFile(f));
    }

    public static ArrayList<String> compareTwoHistograms(Set<String> hist1, Set<String> hist2) {
        ArrayList<String> same = new ArrayList<>();
        for (String s : hist1) {
            if (hist2.contains(s)) {
                same.add(s);
                //System.out.println(s);
            }
        }
        //System.out.println("Size of same text Array: " + same.size());
        return sortArrayList(same);
    }

    public static ArrayList<String> sortArrayList(ArrayList<String> arrList) {
        Collections.sort(arrList, new CustomComparator());
        return arrList;
    }

    public void startComparingFilePairs() {
        for (OneFile myFile : myFiles) {
            for (OneFile myFile1 : myFiles) {
                if (myFile.equals(myFile1)) {
                    continue;
                }
                boolean isThere = false;
                if (pairs.size() > 0) {
                    for (String[] s : pairs) {
                        if (s[0].equals(myFile.getFile().getName())
                                && s[1].equals(myFile1.getFile().getName())) {
                            isThere = true;
                        }
                        if (s[0].equals(myFile1.getFile().getName())
                                && s[1].equals(myFile1.getFile().getName())) {
                            isThere = true;
                        }
                    }
                }

                if (isThere) {
                    continue;
                }

                //System.out.println("OK");
                System.out.println("Comparing:");
                System.out.println(" File 1: " + myFile.getFile().getName());
                System.out.println(" File 2: " + myFile1.getFile().getName());
                ArrayList<String> simil = compareTwoHistograms(myFile.getHist(), myFile1.getHist());

                edges.addEdge(myFile, myFile1, simil);

                pairs.add(new String[]{myFile.getFile().getName(), myFile1.getFile().getName()});
                pairs.add(new String[]{myFile1.getFile().getName(), myFile.getFile().getName()});

//                System.out.println("pairs = " + pairs.toString());
//                System.out.println("");
            }
        }
    }

    public void startComparingEdges() {
        ArrayList<Edge> eds;
        ArrayList<String> res;
        //System.out.println("");
        ArrayList<Class> classes = new ArrayList<>();

        for (OneFile myFile : myFiles) {
            Class cl = new Class(myFile);
            classes.add(cl);

            eds = edges.getEdgesOfFile(myFile);
            //System.out.println("Edges for file: " + myFile.getFile().getName());

            for (Edge ed : eds) {
                if (ed.getSame().size() < 5) {
                    continue;
                }
                //System.out.println(ed.toString());
                for (Class clas : classes) {
                    res = compareTwoHistograms(new HashSet<>(clas.getHist()), new HashSet<>(ed.getSame()));
                    if (res.isEmpty()) {
                        continue;
                    }
                    //printArrayList(res);
                    System.out.println("res size = " + res.size());
                    double perc = (double) res.size() / (double) clas.getHist().size();
                    //System.out.println("perc = " + perc);
                    if (clas.addFile(ed.getFile1()) || clas.addFile(ed.getFile2())) {
                        clas.setHist(res);
                        break;
                    }
                }
            }
            cl.saveAllFiles();
        }
    }

    public static class CustomComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            return o2.length() - o1.length();
        }
    }

}
