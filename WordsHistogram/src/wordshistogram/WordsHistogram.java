package wordshistogram;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author pavlat
 */
public class WordsHistogram {

    public static String mainPath = "c:\\Users\\pavlat\\Documents\\NetBeansProjects\\WordsHistogram\\WordsHistogram\\files\\";
//    public static String mainPath = "c:\\Users\\Adam\\Documents\\NetBeansProjects\\WordsHistogram\\WordsHistogram\\files\\";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        FilesManager fileMan = new FilesManager(new File(mainPath));
        ComparingManager cmpMan = new ComparingManager();

        System.out.println("Path: " + mainPath);
        System.out.println("Number of files: " + fileMan.getNumOfFiles());
        System.out.println("For fully connected graph is number of pairs: " + fileMan.getNumOfConnections());

        try {
            if (fileMan.isDir()) {
                System.out.println("Starting first scan of files...");
                for (File f : fileMan.getFiles()) {
                    if (f.isFile()) {
                        System.out.println("file: " + f);
                        cmpMan.add(f);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        cmpMan.startComparingFilePairs();
        cmpMan.startComparingEdges();
        System.out.println("Parsing files complete in time: " + (System.currentTimeMillis() - startTime) + " milis");

        ToVis tv = new ToVis(cmpMan.getClasses());

    }

    public static void printArrayList(ArrayList<String> arrList) {
        for (String s : arrList) {
            System.out.println(s);
        }
    }

}