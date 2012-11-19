
import java.util.HashMap;

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
    
    // TODO(uvictor)
    public void subtract(FrequencyMap other) {
        
    }
}
