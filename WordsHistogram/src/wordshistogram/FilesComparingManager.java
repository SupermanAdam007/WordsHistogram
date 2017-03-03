package wordshistogram;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Adam
 */
public class FilesComparingManager {

    private final Set<OneFile> myFiles;
    private final Map<String, String> pairs;
    private final EdgesCollector edges;

    public FilesComparingManager() {
        myFiles = new HashSet<>();
        pairs = new HashMap<>();
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
        System.out.println("Size of same text Array: " + same.size());
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
                if (pairs.size() > 0) {
                    if (pairs.containsKey(myFile.getFile().getName())
                            && pairs.get(myFile.getFile().getName()).equals(myFile1.getFile().getName())) {
                        continue;
                    }
                    if (pairs.containsKey(myFile1.getFile().getName())
                            && pairs.get(myFile1.getFile().getName()).equals(myFile.getFile().getName())) {
                        continue;
                    }
                }
                
                //System.out.println("OK");
                System.out.println("Comparing:");
                System.out.println(" File 1: " + myFile.getFile().getName());
                System.out.println(" File 2: " + myFile1.getFile().getName());
                ArrayList<String> simil = compareTwoHistograms(myFile.getHist(), myFile1.getHist());

                edges.addEdge(myFile, myFile1, simil);

                pairs.put(myFile.getFile().getName(), myFile1.getFile().getName());
                pairs.put(myFile1.getFile().getName(), myFile.getFile().getName());
            }
        }
    }

    public void startCreatingClasses() {

    }

    public static class CustomComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            return o1.length() - o2.length();
        }
    }

}
