import java.util.TreeSet;

/**
 * Wrapper to easily represent and sort pairs of type
 * (docId, appearanceIndex) within the DataSet.
 */
public class DocIdEntry implements Comparable {
    private String docId;
    private TreeSet<Integer> positions = new TreeSet<Integer>();

    public DocIdEntry(String docId) {
        this.docId = docId;
    }
    
    public String getDocId() {
        return docId;
    }

    public void addPosition(int pos) {
        positions.add(pos);
    }

    @Override
    public int compareTo(Object entry) {
        if (!entry.getClass().equals(DocIdEntry.class))
            return -1;
        
        return docId.compareTo(((DocIdEntry) entry).getDocId());
    }
}