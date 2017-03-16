package wordshistogram;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Adam
 */
public class ComparingManager {

    private final Set<OneFile> myFiles;
    private final ArrayList<String[]> pairs;
    private final EdgesCollector edges;
    private final ArrayList<Class> classes;

    public ComparingManager() {
        myFiles = new HashSet<>();
        pairs = new ArrayList<>();
        edges = new EdgesCollector();
        classes = new ArrayList<>();
    }

    public void add(File f) {
        myFiles.add(new OneFile(f));
    }

    public static ArrayList<String> compareTwoHistograms(Set<String> hist1, Set<String> hist2) {
        ArrayList<String> same = new ArrayList<>();
        for (String s : hist1) {
            if (hist2.contains(s) && !same.contains(s)) {
                same.add(s);
                //System.out.println(s);
            }
        }
        for (String s : hist2) {
            if (hist1.contains(s) && !same.contains(s)) {
                same.add(s);
                //System.out.println(s);
            }
        }
        //System.out.println("Size of same text Array: " + same.size());
        return sortArrayList(same);
    }

    public ArrayList<String> getCommonElements(final List<String> listA, final List<String> listB) {
        final Map<Integer, List<String>> hashA = new HashMap<>(listA.size());
        final Iterator<String> a = listA.iterator();
        while (a.hasNext()) {
            final String item = a.next();
            List<String> subList = hashA.get(item.hashCode());
            if (subList == null) {
                subList = new ArrayList<>(4);
                hashA.put(item.hashCode(), subList);
            }
            subList.add(item);
        }
        final ArrayList<String> results = new ArrayList<>();
        final Iterator<String> i = listB.iterator();
        while (i.hasNext()) {
            final String item = i.next();
            final List<String> list = hashA.get(item.hashCode());
            if (list != null && list.contains(item)) {
                results.add(item);
            }
        }
        return results;
    }

    public static ArrayList<String> compareTwoHistograms2(ArrayList<String> hist1, ArrayList<String> hist2) {
        Set<String> same = new HashSet<>();
        hist2.retainAll(hist1);
        return hist2;
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
                ArrayList<String> simil = getCommonElements(new ArrayList<>(myFile.getHist()), new ArrayList<>(myFile1.getHist()));

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

        ArrayList<String> bins = new ArrayList<>();
        String bin;
        int maxLen = Integer.toBinaryString((int) Math.pow(2, myFiles.size() - 1)).length();
        for (int i = 1; i < (int) Math.pow(2, myFiles.size()); i++) {
            bin = Integer.toBinaryString(i);
//            System.out.println(bin);
            while (bin.length() < maxLen) {
                bin = "0" + bin;
            }
            bins.add(bin);
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

        System.out.println("");
        double progress = 1;
        int bini = 0;
        int binTmp;
        for (ArrayList<OneFile> re : res) {
//            String strProgress = String.valueOf((double) 100 * progress++ / Math.pow(2, myFiles.size()));
//            System.out.println("-Progress: " + strProgress.substring(0, Math.min(4, strProgress.length())) + " %");

            ArrayList<Edge> edgesNow = new ArrayList<>();
            binTmp = bins.get(bini++).split("1", -1).length - 1;
            int ii = 0;
            int jj = 1;
            Edge e;
            while (binTmp > 1) {
                edgesNow.add(edges.getEdge(re.get(ii), re.get(jj)));
                ii++;
                jj++;
                binTmp -= 2;
            }

            Class cl = new Class();

            for (Edge edge : edgesNow) {
                if (cl.getFiles().isEmpty()) {
                    cl.addFile(edge.getFile1());
                    cl.addFile(edge.getFile2());
                    cl.setHist(edge.getSame());
                } else {
                    cl.addFile(edge.getFile1());
                    cl.addFile(edge.getFile2());
                    cl.setHist(getCommonElements(cl.getHist(), edge.getSame()));
                }
                re.remove(edge.getFile1());
                re.remove(edge.getFile2());
            }

//            System.out.println("re size = " + re.size());
            if (re.size() > 0) {
//                System.out.println("**pridal jeste lichej file");
                cl.addFile(re.get(0));
                cl.setHist(getCommonElements(cl.getHist(), new ArrayList<>(re.get(0).getHist())));
            }

            boolean skip = false;
            Class tmpClass = new Class();
            tmpClass.addAllFiles(cl.getFiles());

            for (Class cl1 : classes) {
                if (tmpClass.getFiles().size() == cl1.getFiles().size()
                        && cl1.isGreater(tmpClass)) {   // vs tmpClass.isGreater(cl1)) {
                    skip = true;
//                    System.out.println("===== rem is true");
                    break;
                }
            }

            if (skip || cl.getHist().isEmpty()) {
                continue;
            }

            System.out.println("classes size = " + classes.size());
            System.out.println(cl.toString());
            classes.add(cl);
        }

//        ArrayList<Class> toRemove = new ArrayList<>();
//        for (Class cl : classes) {
//            if (cl.getFiles().size() == 1) {
//                toRemove.add(cl);
//            }
//        }
//        classes.removeAll(toRemove);
        System.out.println("Saving files...");
        for (Class classe : classes) {
            classe.saveAllFiles();
        }
        System.out.println("Complete");
        //System.out.println("len = " + res.size());
    }

    public ArrayList<Class> getClasses() {
        return classes;
    }

    public static class CustomComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            return o2.length() - o1.length();
        }
    }

}
