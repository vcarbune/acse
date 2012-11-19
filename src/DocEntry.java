
import java.util.HashMap;

/**
 * Wrapper to easily represent and sort pairs of type (docId, frequency) within the DataSet.
 */
// TODO(uvictor)
public class DocEntry implements Comparable<DocEntry> {

    private static class Word {

        private int hamCount;
        private int spamCount;

        public int getHamCount() {
            return hamCount;
        }

        public void incHamCount() {
            this.hamCount++;
        }

        public int getSpamCount() {
            return spamCount;
        }

        public void incSpamCount() {
            this.spamCount++;
        }
    }
    private String docId;
    private HashMap<String, Word> wordCounts;

    public DocEntry(String docId) {
        this.docId = docId;
        this.wordCounts = new HashMap<String, Word>();
    }

    public String getDocId() {
        return docId;
    }

    public int getCount(String term) {
        Word word = wordCounts.get(term);
        //return (double) word.getSpamCount() / (word.getSpamCount() + word.get)
        return 0;
    }

    public void incCount(String word) {
        // TODO(uvictor)
    }   
    
    @Override
    public int compareTo(DocEntry entry) {
        return docId.compareTo(entry.getDocId());
    }

    @Override
    public boolean equals(Object entry) {
        if (!entry.getClass().equals(DocEntry.class)) {
            return false;
        }

        return docId.equals(((DocEntry) entry).getDocId());
    }
}