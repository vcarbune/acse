import java.util.HashMap;
import java.util.TreeSet;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class DataSet {
    private HashMap<String, TreeSet<String>> data = new HashMap<String, TreeSet<String>>();
    private TreeSet<String> docSet = new TreeSet<String>();
    private Logger logger = Logger.getLogger(DataSet.class.getName());

    private void initializeLogging() {
        try {
            FileHandler handler =
                    new FileHandler(Config.STATIC_STATS_FILE, Config.LOG_FILE_SIZE, Config.LOG_FILE_COUNT);
            logger.addHandler(handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DataSet() {
        initializeLogging();
    }
    
    public TreeSet<String> getDocSet() {
        return docSet;
    }

    public void setDocSet(TreeSet<String> docSet) {
        this.docSet = docSet;
    }

    /**
     * Returns the list of documents ids in which a specific term appears.
     * 
     * @param term The term for which to return the doc id list.
     * @return The list of document ids.
     */
    public TreeSet<String> getDocIdSet(String term) {
        return data.get(term);
    }

    /**
     * Marks the appearance of a term in a docId.
     * 
     * @param term The term discovered.
     * @param docId The document id in which it appears.
     */
    public void addPair(String term, String docId) {
        TreeSet<String> docIdList = data.get(term);

        docSet.add(docId);
        
        if (docIdList == null) {
            docIdList = new TreeSet<String>();
            data.put(term, docIdList);
        }

        if (!docIdList.contains(docId)) {
            docIdList.add(docId);
        }
    }

    /**
     * Logs the statistics related to the corpus.
     */
    public void logStaticStats() {
        logger.log(Config.LOG_LEVEL, "Generating Statistics from the Document Corpus\n");
        logger.log(Config.LOG_LEVEL, "The Document Corpus: " + data.keySet().toString() + "\n");
        
        String maxTerm = null;
        String minTerm = null;
        int totalOnes = 0;
        
        for (String term: data.keySet()) {
            int numDocs = data.get(term).size();
            totalOnes += numDocs;
            
            if (maxTerm == null || data.get(maxTerm).size() < numDocs) {
                maxTerm = term;
            }
            if (minTerm == null || data.get(minTerm).size() > numDocs) {
                minTerm = term;
            }
            
        }
        
        logger.log(Config.LOG_LEVEL, "The size of the term-doc matrix: " + data.size() + 
                " terms, " + docSet.size() + " documents.\n");
        
        logger.log(Config.LOG_LEVEL, "The number of ones in the matrix: " + totalOnes + "\n");
        logger.log(Config.LOG_LEVEL, "The longest posting list: " + maxTerm + " - " +
                data.get(maxTerm).size() + "\n");
        logger.log(Config.LOG_LEVEL, "The shortest posting list: " + minTerm + " - " +
                data.get(minTerm).size() + "\n");
        
    }

    
}
