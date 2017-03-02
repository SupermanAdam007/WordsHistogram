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

        FilesManager fileMan = new FilesManager(new File("c:\\Users\\Adam\\Documents\\NetBeansProjects\\WordsHistogram\\WordsHistogram\\files\\"));
        FilesComparingManager fileCmpMan = new FilesComparingManager();
        try {
            if (fileMan.isDir()) {
                System.out.println("Starting first scan of files...");
                for (File f : fileMan.getFiles()) {
                    if (f.isFile()) {
                        System.out.println("file: " + f);
                        fileCmpMan.add(f);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        fileCmpMan.startComparingAll();
        System.out.println("Parsing files complete in time: " + (System.currentTimeMillis() - startTime) + " milis");

    }

    public static void printArrayList(ArrayList<String> arrList) {
        for (String s : arrList) {
            System.out.println(s);
        }
    }

}
