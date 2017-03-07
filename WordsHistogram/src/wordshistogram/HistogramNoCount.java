package wordshistogram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author pavlat
 */
public class HistogramNoCount {

    private final Set<String> hist;
    private final File f;

    public HistogramNoCount(File f) {
        this.f = f;
        this.hist = new HashSet<>();
        makeHist();
    }

    private void makeHist() {
        BufferedReader buffr;
        try {
            buffr = new BufferedReader(new FileReader(f));
            String line;
            String[] split;
            //int[] len = new int[]{5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
            int[] len = new int[]{5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 20, 40};
            while ((line = buffr.readLine()) != null) { //every row from 4 to 10 letters substrings
                line = line.toUpperCase();
                for (int i : len) {
                    for (int j = 0; j < line.length() - i; j += i) {
                        for (int k = 0; k < i / 2; k++) {
                            String resResLine = line.substring(Math.max(j + k, 0), Math.min(j + i + k, line.length())).trim();
                            String resLine;
                            //System.out.println(resResLine);
                            if (((resLine = resResLine).length() >= len[0]) & !hist.contains(resLine)) {
                                hist.add(resLine);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveHistToFile() {
        try {
            FileWriter fw = new FileWriter(f.getAbsolutePath().split("\\.")[0] + "_wordsSet.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            for (String s : hist) {
                out.write(s + "\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void printRes() {
        System.out.println(resToString());
    }

    public String resToString() {
        return hist.toString();
    }

    public Set<String> getHist() {
        return hist;
    }
}
