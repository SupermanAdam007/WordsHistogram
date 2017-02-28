package wordshistogram;

import java.io.File;

/**
 *
 * @author pavlat
 */
public class WordsHistogram {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("You must enter a path to dir as argument...");
            return;
        }

        System.out.println("Path to dir: " + args[0]);

        try {
            File dir = new File(args[0]);
            if (dir.isDirectory()) {
                for (File f : dir.listFiles()) {
                    if (f.isFile()) {
                        System.out.println("file: " + f);
                        Histogram hist = new Histogram(f);
                        hist.printRes();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
