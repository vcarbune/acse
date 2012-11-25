
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DocEntry {

    private String docId;
    private Boolean spam;
    private HashMap<String, Integer> wordCounts;
    private HashMap<String, Double> wordWeights= null; // tf-idf weights
    private double vectorLength;

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
        incCount(word, 1);
	}
    
    public void incCount(String word, int inc) {
        if(wordCounts.get(word) == null)
            wordCounts.put(word, inc);
        else{
            int count = wordCounts.get(word);
            wordCounts.put(word, count + inc);
        }
    }
    
    public Set<Map.Entry<String, Integer>> getWordCounts() {
        return wordCounts.entrySet();
    }
    
    public Set<String> getWords() {
        return wordCounts.keySet();
    }
    
    /**
     * For phase 2.
     *  
     * Computes the map with the tf-idf weights for each word in the doc,
     * using a map tha contains the df values for each term.
     * 
     * @param docFrequencyMap df values for each term
     * @param numDocs N in the tf-idf formula
     */
    public void computeWordWeights(HashMap<String, Integer> docFrequencyMap, int numDocs) {
        
        if (wordCounts.size() == 0) {
            throw new RuntimeException("The wordCounts map is empty!");
        }
        
        wordWeights = new HashMap<String, Double>();
        vectorLength = 0;
        
        for (String term: wordCounts.keySet()) {
            double tf_idf = (1 + Math.log10(wordCounts.get(term))) * 
                    Math.log10(numDocs / (double) docFrequencyMap.get(term));
            wordWeights.put(term, tf_idf);
            vectorLength += tf_idf * tf_idf;
        }
        
        vectorLength = Math.sqrt(vectorLength);
    }
    
    /**
     * For phase 2.
     * 
     * Computes the distance between 2 documents, 
     * 
     * @param other
     * @return
     */
    public double getDistance(DocEntry other) {
        //TODO: which formula? cosine doesn't seem suitable here...
        return 0;
    }
    
    public static void main(String[] args) {
        
        // Testing computeWordWeights
        DocEntry doc = new DocEntry("doc1", true);
        doc.incCount("asd", 100);
        doc.incCount("qwe", 10);
        HashMap<String, Integer> dfMap = new HashMap<String, Integer>();
        dfMap.put("asd", 10);
        dfMap.put("qwe", 1);
        doc.computeWordWeights(dfMap, 100);
        
        System.out.println(doc.wordWeights);
        System.out.println(doc.vectorLength);
    }
}
