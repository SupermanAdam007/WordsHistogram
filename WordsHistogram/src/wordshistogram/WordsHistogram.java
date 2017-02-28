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
        long startTime = System.currentTimeMillis();

        /*if (args.length == 0) {
            System.out.println("You must enter a path to dir as argument...");
            return;
        }

        System.out.println("Path to dir: " + args[0]);*/
        try {
//            File dir = new File(args[0]);
            File dir = new File("C:\\Users\\Adam\\Documents\\NetBeansProjects\\WordsHistogram\\WordsHistogram\\files");
            if (dir.isDirectory()) {
                for (File f : dir.listFiles()) {
                    if (f.isFile()) {
                        System.out.println("file: " + f);
                        Histogram hist = new Histogram(f);
                        hist.saveHistToFile(0, 999);
                        
                        /*if (args[1].length() < 2) {
                            System.out.println("From 0 to 999: ");
                            System.out.println(hist.printRange(0, 999));
                        } else {
                            System.out.println("From " + args[1] + " to " + args[2] + ": ");
                            System.out.println(hist.printRange(Integer.valueOf(args[1]), Integer.valueOf(args[2])));
                        }*/
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("In time: " + (System.currentTimeMillis() - startTime) + " milis");
    }

}
