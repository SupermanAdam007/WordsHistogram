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

    private Set<String> hist;
    private File f;

    public HistogramNoCount(File f) {
        this.f = f;
        this.hist = new HashSet<String>();
        makeHist();
    }

    private void makeHist() {
        BufferedReader buffr;
        try {
            buffr = new BufferedReader(new FileReader(f));
            String line;
            String[] split;
            int[] len = new int[]{5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
            while ((line = buffr.readLine()) != null) { //every row from 4 to 10 letters substrings
                for (int i : len) {
                    for (int j = 0; j < line.length() - i; j += i) {
                        String resLine;
                        if ((resLine = line.substring(j, j + i).trim()).length() >= len[0]) {
                            hist.add(resLine);
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
