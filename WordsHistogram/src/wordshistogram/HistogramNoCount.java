package wordshistogram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
//            int[] len = new int[]{10};
            int[] len = new int[]{4, 5, 6, 7, 8, 9, 10, 15, 20, 30};
            ArrayList<String> blackList = getBlackList();
            boolean blackListed;

            while ((line = buffr.readLine()) != null) {
                line = line.toUpperCase();
                for (int i : len) {
                    for (int j = 0; j < line.length() - i; j += i) {
                        for (int k = 0; k < i / 2; k++) {
                            String resResLine = line.substring(Math.max(j + k, 0), Math.min(j + i + k, line.length())).trim();
                            blackListed = false;
                            for (String s : blackList) {
                                if (resResLine.contains(s)) {
                                    blackListed = true;
                                    break;
                                }
                            }
                            if (blackListed || resResLine.replace(" ", "").length() < len[0]) {
                                continue;
                            }
                            hist.add(resResLine);
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

    private ArrayList<String> getBlackList() {
        ArrayList<String> blackList = new ArrayList<>();
        blackList.add("CTIO");
        blackList.add("TION");
        blackList.add("ION ");
        blackList.add("FUNC");
        blackList.add("UNCTI");
        blackList.add("UNCT");
        blackList.add("TRIN");
        blackList.add("STRI");
        blackList.add("RING");
        blackList.add("ETUR");
        blackList.add("DEFI");
        blackList.add("NUMB");
        blackList.add("UMBE");
        blackList.add("VAR ");
        blackList.add("�");
        blackList.add("MANIF");
        blackList.add("ENT-TYP");
        blackList.add("NTEN");
        blackList.add("CONT");
        blackList.add("EXT/HTM");
        blackList.add("TYPE");
        blackList.add("TEXT");
        blackList.add("OCUMEN");
        blackList.add("AVASC");
        blackList.add("HTML");
        blackList.add("CRIP");
        blackList.add("SCRI");
        blackList.add("TURN");
        blackList.add("RETU");
        blackList.add("RIPT");
        return blackList;
    }
}
