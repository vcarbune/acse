import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DocSet extends FrequencyMap {
    // Hashmap from Term to number of docs in which appears.
    HashMap<String, Integer> docFrequencyMap = new HashMap<String, Integer>();

    private ArrayList<DocEntry> docEntries = new ArrayList<DocEntry>();
    private int numSpamDocs;
    
    
    public DocSet(){
    	numSpamDocs = 0;
    }
    
    public int getDocEntriesSize() {
        return docEntries.size();
    }

    public Iterator<DocEntry> getDocEntriesIterator() {
        return docEntries.iterator();
    }
    
    public DocEntry getDocEntry(int i) {
        return docEntries.get(i);
    }

    public void addDoc(final DocEntry docEntry) {
        docEntries.add(docEntry);

        boolean spam = docEntry.isSpam();

        if (spam) {
            numSpamDocs++;
        }

        for (Map.Entry<String, Integer> wordCount : docEntry.getWordCounts()) {
            addWord(spam, wordCount.getKey(), wordCount.getValue());

            Integer docsCount = docFrequencyMap.get(wordCount.getKey());
            docsCount = docsCount == null ? 1 : docsCount + 1;

            docFrequencyMap.put(wordCount.getKey(), docsCount);
        }
    }
    
    public void precomputeWordWeights() {
        int docEntriesSize = docEntries.size();
        for (DocEntry entry : docEntries) {
            entry.computeWordWeights(docFrequencyMap, docEntriesSize);
        }
    }

    public int getNumSpamDocs() {
        return numSpamDocs;
    }
    
    public int getNumHamDocs() {
        return docEntries.size() - numSpamDocs;
    }
    
    public int getNumTotalDocs() {
        return docEntries.size();
    }
}
