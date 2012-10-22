import java.util.HashMap;
import java.util.TreeSet;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class DataSet {
    private HashMap<String, TreeSet<DocIdEntry>> data = new HashMap<String, TreeSet<DocIdEntry>>();
    private TreeSet<String> docSet = new TreeSet<String>();
    private Logger logger = Logger.getLogger(DataSet.class.getName());

    //TODO: redesign the data structures
    
    
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
        TreeSet<DocIdEntry> entryList = data.get(term);

        TreeSet<String> result = new TreeSet<String>();
        if(entryList != null){
            for (DocIdEntry entry : entryList) {
                result.add(entry.getDocId());
            }
        }

        return result;
    }


    public TreeSet<DocIdEntry> getDocIdEntrySet(String term){
        return data.get(term);
    }

    
    //TODO: change this method, should update the frequencies
    /**
     * Marks the appearance of a term in a docId.
     * 
     * @param term The term discovered.
     * @param docId The document id in which it appears.
     * @param position The position within the document.
     */
    public void addPair(String term, String docId, int position) {
        TreeSet<DocIdEntry> docIdList = data.get(term);
        docSet.add(docId);

        if (docIdList == null) {
            docIdList = new TreeSet<DocIdEntry>();
            data.put(term, docIdList);
        }
        
        DocIdEntry docIdEntry = new DocIdEntry(docId);
        
        DocIdEntry lowerDocIdEntry = docIdList.floor(docIdEntry);
        if (lowerDocIdEntry != null && lowerDocIdEntry.getDocId().equals(docIdEntry.getDocId())) {
            docIdEntry = lowerDocIdEntry;
        } else {
            docIdList.add(docIdEntry);
        }
        
        docIdEntry.addPosition(position);
    }
}