
import java.util.ArrayList;
import java.util.Map;

public class DocSet extends FrequencyMap {

    ArrayList<DocEntry> docEntries;
    private int numSpamDocs;
    
    public DocSet(){
    	docEntries = new ArrayList<DocEntry>();
    	numSpamDocs = 0;
    }

    public void addDoc(final DocEntry docEntry) {
        docEntries.add(docEntry);

        boolean spam = docEntry.isSpam();
        
        if (spam) {
            numSpamDocs++;
        }
        
        for (Map.Entry<String, Integer> wordCount : docEntry.getWordCounts()) {
            addWord(spam, wordCount.getKey(), wordCount.getValue());
        }
    }
    
    public int getNumSpamDocs() {
        return numSpamDocs;
    }
    
    public int getNumHamDocs() {
        return docEntries.size() - numSpamDocs;
    }
}
