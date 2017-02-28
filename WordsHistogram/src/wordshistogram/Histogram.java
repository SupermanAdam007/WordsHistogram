package wordshistogram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author pavlat
 */
public class Histogram {

    private final HashMap<String, Integer> hist;
    private final TreeMap<String, Integer> histSorted;
    private File f;

    public Histogram(File f) {
        this.f = f;
        this.hist = new HashMap<>();
        ValueComparator bvc = new ValueComparator(hist);
        histSorted = new TreeMap<>(bvc);
        makeHist();
    }

    private void makeHist() {
        BufferedReader buffr;
        try {
            buffr = new BufferedReader(new FileReader(f));
            String word;
            String[] split;
            Integer freq;
            while ((word = buffr.readLine()) != null) {
                split = word.split("\\W+");
                for (String s : split) {
                    freq = hist.get(s);
                    if (freq == null) {
                        hist.put(s.toUpperCase(), 1);
                    } else {
                        hist.put(s.toUpperCase(), freq + 1);
                    }
                }
            }

            histSorted.putAll(hist);
//            System.out.println("ssssssssssssssssssssssss = " + histSorted.size());
//            System.out.println(histSorted.toString().replace(" ", "").replace(",", "\n").replace("=", " = "));
//            System.out.println("sssssssssssssssssssssssss = " + hist.size());
//            System.out.println(hist.toString().replace(" ", "").replace(",", "\n").replace("=", " = "));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String resToString() {
        System.out.println("");
        String textRes = "Histogram for file: " + f.getAbsolutePath() + ":\n";
        for (String poss : histSorted.keySet()) {
            String value = String.valueOf(histSorted.get(poss));
            textRes += poss + " " + value + "\n";
        }
        return textRes;
    }

    public String printRange(int from, int to) {
        System.out.println("");
        String textRes = "Histogram for file: " + f.getAbsolutePath() + ":\n";
        for (String poss : hist.keySet()) {
            String value = String.valueOf(hist.get(poss));
            if (Integer.valueOf(value) >= from && Integer.valueOf(value) <= to) {
                textRes += poss + " " + value + "\n";
            }
        }
        return textRes;
    }

    public void saveHistToFile(int from, int to) {
        try (FileWriter fw = new FileWriter(f.getAbsolutePath().split("\\.")[0] + "_wordFreq.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            Set set = histSorted.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry) iterator.next();
//                System.out.print("key is: " + mentry.getKey() + " & Value is: ");
//                System.out.println(mentry.getValue());
                if (mentry.getKey().toString().length() >= 4
                        && Integer.valueOf(String.valueOf((int) mentry.getValue())) >= from
                        && Integer.valueOf(String.valueOf((int) mentry.getValue())) <= to) {
                    out.write(mentry.getKey().toString() + " " + mentry.getValue() + "\n");
                }
            }

        } catch (IOException e) {
        }
    }

    public HashMap<String, Integer> getHist() {
        return hist;
    }

    class ValueComparator implements Comparator<String> {

        Map<String, Integer> base;

        public ValueComparator(Map<String, Integer> base) {
            this.base = base;
        }

        @Override
        public int compare(String a, String b) {
            if (base.get(a) >= base.get(b)) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
