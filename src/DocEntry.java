
/**
 * Wrapper to easily represent and sort pairs of type
 * (docId, appearanceIndex) within the DataSet.
 */
public class DocEntry implements Comparable<DocEntry> {
    private String docId;
    private int frequency;

    public DocEntry(String docId) {
        this.docId = docId;
        this.frequency = 1;
    }
    
    public String getDocId() {
        return docId;
    }
    
    public int getFrequency() {
        return frequency;
    }

    public void incFrequency() {
        frequency++;
    }

    @Override
    public int compareTo(DocEntry entry) {
        return docId.compareTo(entry.getDocId());
    }
    
    @Override
    public boolean equals(Object entry) {
        if (!entry.getClass().equals(DocEntry.class))
            return false;
        
        return docId.equals(((DocEntry) entry).getDocId());
    }
}