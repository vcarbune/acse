
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DocEntry {

    private String docId;
    private Boolean spam;
    private HashMap<String, Integer> wordCounts;

    public DocEntry(String docId, Boolean spam) {
        this.docId = docId;
        this.spam = spam;
        this.wordCounts = new HashMap<String, Integer>();
    }

    public String getDocId() {
        return docId;
    }
    
    /**
     * @throws NullPointerException if we don't know yet if document is spam or not
     */
    public boolean isSpam() {
        return spam;
    }

    public int getCount(String term) {
        return wordCounts.get(term);
    }

    public void incCount(String word) {
        int count = wordCounts.get(word);
        wordCounts.put(word, count + 1);
    }
    
    public Set<Map.Entry<String, Integer>> getWordCounts() {
        return wordCounts.entrySet();
    }
    
    public Set<String> getWords() {
        return wordCounts.keySet();
    }
}
