package wordshistogram;

import java.io.File;

/**
 *
 * @author Adam
 */
public class FilesManager {

    private final File dir;
    private boolean isDir = false;
    private File[] files;

    public FilesManager(File dir) {
        this.dir = dir;
        this.files = null;
        if (dir.isDirectory()) {
            this.files = dir.listFiles();
            this.isDir = true;
        } else {
            System.err.println("Error: \"" + dir.getAbsolutePath() + "\" is not directory...");
        }
    }

    public File[] getFiles() {
        return files;
    }

    public File getDir() {
        return dir;
    }

    public boolean isDir() {
        return isDir;
    }

    public int getNumOfFiles() {
        return this.files.length;
    }

    public int getNumOfConnections() {
        return this.files.length * (this.files.length - 1) / 2;
    }

}
