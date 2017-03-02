package wordshistogram;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author pavlat
 */
public class WordsHistogram {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        /*if (args.length == 0) {
            System.out.println("You must enter a path to dir as argument...");
            return;
        }

        System.out.println("Path to dir: " + args[0]);*/
        ArrayList<HistogramNoCount> hists = new ArrayList<>();
        try {
//            File dir = new File(args[0]);
            File dir = new File("C:\\Users\\pavlat\\Documents\\NetBeansProjects\\WordsHistogram\\WordsHistogram\\files");
            if (dir.isDirectory()) {
                for (File f : dir.listFiles()) {
                    if (f.isFile()) {
                        System.out.println("file: " + f);
                        //HistogramNoCount hist = new HistogramNoCount(f);
                        //hist.saveHistToFile();
                        hists.add(new HistogramNoCount(f));
                    }
                }
                compareTwoHistograms(hists.get(0), hists.get(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("Parsing files complete in time: " + (System.currentTimeMillis() - startTime) + " milis");
        
    }
    
    public static ArrayList<String> compareTwoHistograms(HistogramNoCount hist1, HistogramNoCount hist2) {
        ArrayList<String> same = new ArrayList<>();
        for (String s : hist1.getHist()) {
            if (hist2.getHist().contains(s)) {
                same.add(s);
                System.out.println(s);
            }
        }
        return same;
    }
    
}
