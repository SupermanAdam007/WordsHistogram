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
public class FilesComparingManager {

    private final Set<OneFile> myFiles;
    private Set<File[]> pairs;

    public FilesComparingManager() {
        myFiles = new HashSet<>();
        pairs = new HashSet<>();
    }

    public void add(File f) {
        myFiles.add(new OneFile(f));
    }

    public ArrayList<String> compareTwoHistograms(Set<String> hist1, Set<String> hist2) {
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

    private ArrayList<String> sortArrayList(ArrayList<String> arrList) {
        Collections.sort(arrList, new CustomComparator());
        return arrList;
    }

    public void startComparingAll() {
        for (OneFile myFile : myFiles) {
            for (OneFile myFile1 : myFiles) {
                if (myFile.equals(myFile1)) {
                    continue;
                } //else if (pairs.size() < 2) {
                System.out.println("Comparing:");
                System.out.println(" File 1: " + myFile.getFile().getName());
                System.out.println(" File 2: " + myFile1.getFile().getName());
                //pairs.add(new File[]{myFile.getFile(), myFile1.getFile()});
                compareTwoHistograms(myFile.getHist(), myFile1.getHist());
                //}
            }
        }
    }

    public static class CustomComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            return o1.length() - o2.length();
        }
    }

}
