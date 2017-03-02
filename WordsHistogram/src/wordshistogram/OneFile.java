package wordshistogram;

import java.io.File;
import java.util.Set;

/**
 *
 * @author Adam
 */
public class OneFile {

    private final File file;
    private final HistogramNoCount histNC;

    public OneFile(File file) {
        this.file = file;
        this.histNC = new HistogramNoCount(file);
    }

    public Set<String> getHist() {
        return histNC.getHist();
    }

    public File getFile() {
        return file;
    }
    
}
