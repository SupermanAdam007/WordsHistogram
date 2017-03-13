package wordshistogram;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

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
        ArrayList<OneFile> arrFiles = new ArrayList<>();
        for (OneFile myFile : myFiles) {
            arrFiles.add(myFile);
        }
        ArrayList<ArrayList<OneFile>> res = new ArrayList<>();

        String bin;
        int maxLen = Integer.toBinaryString((int) Math.pow(2, myFiles.size() - 1)).length();
        for (int i = 1; i < Math.pow(2, myFiles.size()); i++) {
            bin = Integer.toBinaryString(i);
            while (bin.length() != maxLen) {
                bin = "0" + bin;
            }
            ArrayList<OneFile> ress = new ArrayList<>();
//            System.out.println("bin = " + bin);
            for (int j = 0; j < maxLen; j++) {
                if (bin.charAt(j) == '1') {
                    ress.add(arrFiles.get(j));
                }
            }
//            System.out.println(ress.toString());
            res.add(ress);
        }

        ArrayList<Class> classes = new ArrayList<>();

        System.out.println("");
        double progress = 1;
        for (ArrayList<OneFile> re : res) {
            String strProgress = String.valueOf((double) 100 * progress++ / Math.pow(2, myFiles.size()));
            System.out.println("Progress: " + strProgress.substring(0, Math.min(4, strProgress.length())) + " %");
            for (OneFile oneFile : re) {
                //System.out.print(oneFile.getFile().getName().substring(0, 5) + "-");
            }
            //System.out.println("");

            Class cl = new Class(re.get(0));
            if (re.size() != 1) {
                ArrayList<String> hist;
                for (int i = 1; i < re.size(); i++) {
                    hist = compareTwoHistograms(new HashSet<>(cl.getHist()),
                            re.get(i).getHist());
                    cl.setHist(hist);
                    cl.addFile(re.get(i));
                }
            }
            if (cl.getHist().size() > 10) {
                classes.add(cl);
            }
        }

        System.out.println("Saving files...");
        for (Class classe : classes) {
            classe.saveAllFiles();
        }
        System.out.println("Complete");
        //System.out.println("len = " + res.size());
    }

    public Set<OneFile> getMyFiles() {
        return myFiles;
    }

    public static class CustomComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            return o2.length() - o1.length();
        }
    }

}