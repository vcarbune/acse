import java.util.TreeSet;

/**
 * Wrapper to easily represent and sort pairs of type
 * (docId, appearanceIndex) within the DataSet.
 */
public class DocIdEntry implements Comparable<DocIdEntry> {
    private String docId;
    private TreeSet<Integer> positions = new TreeSet<Integer>();

    public DocIdEntry(String docId) {
        this.docId = docId;
    }
    
    public DocIdEntry(String docId, TreeSet<Integer> positions) {
        this.docId = docId;
        this.positions = positions;
    }
    
    public String getDocId() {
        return docId;
    }

    public void addPosition(int pos) {
        positions.add(pos);
    }

    public TreeSet<Integer> getPositions() {
        return positions;
    }

    @Override
    public int compareTo(DocIdEntry entry) {
        return docId.compareTo(entry.getDocId());
    }
    
    @Override
    public boolean equals(Object entry) {
        if (!entry.getClass().equals(DocIdEntry.class))
            return false;
        
        return docId.equals(((DocIdEntry) entry).getDocId());
    }
}