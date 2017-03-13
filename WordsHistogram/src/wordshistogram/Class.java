package wordshistogram;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import static wordshistogram.WordsHistogram.mainPath;

/**
 *
 * @author pavlat
 */
public class Class {

    private final ArrayList<OneFile> files;
    private ArrayList<String> hist;
    private String trueName;

    public Class() {
        files = new ArrayList<>();
        hist = new ArrayList<>();
        //createFreeNameFolder();
    }

    public Class(OneFile file) {
        files = new ArrayList<>();
        hist = new ArrayList<>();
        files.add(file);
        hist.addAll(file.getHist());
    }

    public Class(ArrayList<OneFile> newFiles) {
        files = new ArrayList<>();
        hist = new ArrayList<>();
        files.addAll(newFiles);
    }

    public boolean addFile(OneFile file) {
        boolean alreadyHere = !files.contains(file);
        if (alreadyHere) {
            this.files.add(file);
        }
        return alreadyHere;
    }

    public void addAllFiles(ArrayList<OneFile> newArr) {
        this.files.addAll(newArr);
    }

    public ArrayList<String> getHist() {
        return hist;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setHist(ArrayList<String> hist) {
        this.hist = hist;
    }

    public ArrayList<OneFile> getFiles() {
        return files;
    }

    private void createFreeNameFolder() {
        File f;
        int i = 65;
        String name;
        String prefix = "";

        while (true) {
            name = Character.toString((char) i++);
            if (name.equals("Z")) {
                prefix += "Z";
                i = 65;
                name = Character.toString((char) i++);
            }
            trueName = mainPath + "res\\" + prefix + name + "\\";
            f = new File(trueName);
            if (!f.exists()) {
                f.mkdirs();
                break;
            }
        }
    }

    public void saveHistToFile() {
        if (files.isEmpty() || hist.size() < 10) {
            return;
        }

        try {
            File f;
            int i = 65;
            String name;
            String postfix = "";
            String fullName = "";

            while (true) {
                name = Character.toString((char) i++);
                if (name.equals("Z")) {
                    postfix += "Z";
                    i = 65;
                    name = Character.toString((char) i++);
                }
                fullName = mainPath + "res\\" + "wordsSet_" + files.size() + postfix + name + ".txt";
                f = new File(fullName);
                if (!f.exists()) {
                    break;
                }
            }

            FileWriter fw = new FileWriter(fullName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            ArrayList<String> histSorted = ComparingManager.sortArrayList(hist);
            for (String s : histSorted) {
                out.write(s + "\n");
            }
            out.close();
            bw.close();
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void saveAllFiles() {
        new File(mainPath + "res\\").mkdir();
        /*trueName = mainPath + "res\\";
        new File(trueName).mkdir();*/
        //createFreeNameFolder();
        /*for (OneFile file : files) {
            try {
                Files.copy(file.getFile().toPath(),
                        new File(trueName + file.getFile().getName()).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }*/
        //System.out.println("Size of hist Class: " + hist.size());
        saveHistToFile();
    }
}
