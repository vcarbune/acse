import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class FrequencyMap {

    private static class WordCount {

        private int hamCount;
        private int spamCount;

        public WordCount(int hamCount, int spamCount) {
            this.hamCount = hamCount;
            this.spamCount = spamCount;
        }

        public int getHamCount() {
            return hamCount;
        }

        public void addHamCount(int count) {
            this.hamCount += count;
        }

        public int getSpamCount() {
            return spamCount;
        }

        public void addSpamCount(int count) {
            this.spamCount += count;
        }
    }
    
    
    private HashMap<String, WordCount> wordCounts;
    
    public FrequencyMap(){
    	wordCounts = new HashMap<String, WordCount>();
    }

    protected void addWord(final boolean spam, final String wordId, final int count) {
        int spamCount = 0;
        int hamCount = 0;
        if (spam) {
            spamCount += count;
        } else {
            hamCount += count;
        }

        WordCount wordCount = wordCounts.get(wordId);
        if (wordCount == null) {
            // We found a new word
            wordCounts.put(wordId, new WordCount(hamCount, spamCount));
        } else {
            // Word already existed
            wordCount.addHamCount(hamCount);
            wordCount.addSpamCount(spamCount);
        }
    }

    public List<Integer> getTotalCounts() {
        int totalHamCount = 0;
        int totalSpamCount = 0;

        for (String word : wordCounts.keySet()) {
            totalHamCount   += wordCounts.get(word).getHamCount();
            totalSpamCount  += wordCounts.get(word).getSpamCount(); 
        }

        return Arrays.asList(totalHamCount, totalSpamCount);
    }

    public int getWordHamCount(final String wordId) {
        return wordCounts.get(wordId).getHamCount();
    }

    public int getWordSpamCount(final String wordId) {
        return wordCounts.get(wordId).getSpamCount();
    }

    public int getVocabularySize() {
        return wordCounts.size();
    }
    
    // TODO(uvictor)
    public void add(FrequencyMap other) {
        
    }
    
    public FrequencyMap subtract(FrequencyMap other) {
        HashMap<String, WordCount> wordCountCopy = new HashMap<String, WordCount>();
        wordCountCopy.putAll(wordCounts);
         
        if(other == null){
        	FrequencyMap fMap = new FrequencyMap();
        	fMap.setWordCounts(wordCountCopy);	
        	return fMap;
        }
        
        List<String> toRemoveWords = new ArrayList<String>();
        for(Entry<String, WordCount> entry : wordCountCopy.entrySet()){
        	String term = entry.getKey();
        	WordCount count = entry.getValue();
        	
        	int subHam = other.getWordHamCount(term);
        	int subSpam = other.getWordSpamCount(term);
        	count.addHamCount((-1) * subHam);
        	count.addSpamCount((-1) * subSpam);
        	
        	if(count.getHamCount() <= 0 && count.getSpamCount() <= 0){
        		toRemoveWords.add(term);
        	}
        	entry.setValue(count); 	
        }
        
        for(String term : toRemoveWords){
        	wordCountCopy.remove(term);
        }
        
        FrequencyMap fMap = new FrequencyMap(); 
        fMap.setWordCounts(wordCountCopy);
        
       return fMap;
    }
    
    public void setWordCounts(HashMap<String, WordCount> wordCounts){
    	this.wordCounts = wordCounts;
    }
}
