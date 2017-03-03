package wordshistogram;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Adam
 */
public class OneFile {

    private final File file;
    private final HistogramNoCount histNC;
    private final Map<OneFile, ArrayList<String>> cmpToOthers;

    public OneFile(File file) {
        this.file = file;
        this.histNC = new HistogramNoCount(file);
        this.cmpToOthers = new HashMap<>();
    }

    public void addCmp(OneFile of, ArrayList<String> simil) {
        this.cmpToOthers.put(of, simil);
    }

    public Set<String> getHist() {
        return histNC.getHist();
    }

    public File getFile() {
        return file;
    }

}
