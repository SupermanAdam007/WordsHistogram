package wordshistogram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author pavlat
 */
public class Histogram {

    private HashMap<String, Integer> hist;
    private File f;

    public Histogram(File f) {
        this.f = f;
        this.hist = new HashMap<>();
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
                freq = hist.get(word);
                if (freq == null) {
                    hist.put(word, 1);
                } else {
                    hist.put(word, freq + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printRes() {
        System.out.println(resToString());
    }

    public String resToString() {
        String textRes = "Histogram for file: " + f.getAbsolutePath() + ":\n";
        for (String poss : hist.keySet()) {
            String value = String.valueOf(hist.get(poss));
            textRes += "      " + poss + " " + value + "\n";
        }
        return textRes;
    }

    public HashMap<String, Integer> getHist() {
        return hist;
    }
}
