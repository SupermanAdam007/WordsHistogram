package wordshistogram;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Adam
 */
public class FilesComparingManager {

    private final HashMap<File, Set<String>> allFilesHist;

    public FilesComparingManager() {
        this.allFilesHist = new HashMap<>();
    }

    public void add(File f, Set<String> hist) {
        allFilesHist.put(f, hist);
    }

    public static ArrayList<String> compareTwoHistograms(HistogramNoCount hist1, HistogramNoCount hist2) {
        ArrayList<String> same = new ArrayList<>();
        for (String s : hist1.getHist()) {
            if (hist2.getHist().contains(s)) {
                same.add(s);
                //System.out.println(s);
            }
        }
        return same;
    }

    private ArrayList<String> sortArrayList(ArrayList<String> arrList) {
        Collections.sort(arrList, new CustomComparator());
        return arrList;
    }

    public static class CustomComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            return o1.length() - o2.length();
        }
    }

}
