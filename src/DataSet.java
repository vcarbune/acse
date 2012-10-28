
import java.util.HashMap;
import java.util.TreeSet;

public class DataSet {

    private HashMap<String, TreeSet<DocEntry>> data = new HashMap<String, TreeSet<DocEntry>>();
    private TreeSet<String> docSet = new TreeSet<String>();
    private HashMap<String, Double> docLengths = new HashMap<String, Double>();

    public DataSet() {
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
        TreeSet<DocEntry> entryList = data.get(term);

        TreeSet<String> result = new TreeSet<String>();
        if (entryList != null) {
            for (DocEntry entry : entryList) {
                result.add(entry.getDocId());
            }
        }

        return result;
    }

    public TreeSet<DocEntry> getDocIdEntrySet(String term) {
        return data.get(term);
    }

    /**
     * Marks the appearance of a term in a docId.
     *
     * @param term The term discovered.
     * @param docId The document id in which it appears.
     */
    public void addPair(String term, String docId) {
        TreeSet<DocEntry> docIdList = data.get(term);
        docSet.add(docId);

        if (docIdList == null) {
            docIdList = new TreeSet<DocEntry>();
            data.put(term, docIdList);
        }

        DocEntry docEntry = new DocEntry(docId);
        DocEntry lowerDocEntry = docIdList.floor(docEntry);
        if (lowerDocEntry != null && lowerDocEntry.getDocId().equals(docEntry.getDocId())) {
            docEntry = lowerDocEntry;
        } else {
            docIdList.add(docEntry);
        }

        docEntry.incFrequency();
    }
    
    /**
     * Computes the lengths of all the document vectors
     */
    public void computeDocLengths() {
        
        for (String term: data.keySet()) {
            for (DocEntry entry: data.get(term)) {
                String docId = entry.getDocId();
                double di = computeDocWeight(docId, term);
                
                if (docLengths.get(docId) != null) {
                    docLengths.put(docId, docLengths.get(docId) + di * di);
                }
                else {
                    docLengths.put(docId, di * di);
                }
            }
        }
        
        for (String docId: docLengths.keySet()) {
            docLengths.put(docId, Math.sqrt(docLengths.get(docId)));
            //System.out.println(docId + " -->\t\t" + docLengths.get(docId));
        }
    }
    
    /**
     * Gets the length of a document vector
     * @param docId
     * @return
     */
    public double getDocLength(String docId) {
        return docLengths.get(docId);
    }

    /**
     * Returns the term frequency for a term in a document: tf
     *
     * @param docId
     * @param term
     * @return
     */
    public int getTermFrequency(String docId, String term) {
        DocEntry docEntry = new DocEntry(docId);
        DocEntry lowerDocEntry = data.get(term).floor(docEntry);

        if (lowerDocEntry != null && lowerDocEntry.getDocId().equals(docEntry.getDocId())) {
            return lowerDocEntry.getFrequency();
        } else {
            return 0;
        }
    }

    /**
     * Returns the document frequency of a term: df
     *
     * @param term
     * @return
     */
    public int getDocFrequency(String term) {
        if (data.get(term) != null) {
            return data.get(term).size();
        }
        else {
            return 1; // Hack. The product qi*di will be zero anyway.
        }
    }

    /**
     * Computes the tf-idf score for a document
     *
     * @param docId
     * @param term
     * @return
     */
    public double computeDocWeight(String docId, String term) {
        if (!getDocIdSet(term).contains(docId)) {
            return 0;
        }

        int N = docSet.size();
        int tf = getTermFrequency(docId, term);
        
        if (tf == 0) {
            return 0;
        }
        
        int df = getDocFrequency(term);

        return (1 + Math.log10(tf)) * Math.log10(N / (double) df);
    }

    /**
     * Computes the tf-idf score for a document
     *
     * @param docId
     * @param term
     * @return
     */
    public double computeQueryWeight(String term, int tf) {
        int N = docSet.size();
        int df = getDocFrequency(term);

        return (1 + Math.log10(tf)) * Math.log10(N / (double) df);
    }
}