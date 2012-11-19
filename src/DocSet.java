
import java.util.ArrayList;
import java.util.Map;

// TODO: complete this with what is needed
public class DocSet extends FrequencyMap {

    ArrayList<DocEntry> docEntries;

    public void addDoc(final DocEntry docEntry) {
        docEntries.add(docEntry);

        boolean spam = docEntry.isSpam();
        for (Map.Entry<String, Integer> wordCount : docEntry.getWordCounts()) {
            addWord(spam, wordCount.getKey(), wordCount.getValue());
        }
    }
}
