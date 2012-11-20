import java.util.ArrayList;


public class Classifier {
    private int truePositives = 0;
    private int trueNegatives = 0;
    private int falsePositives = 0;
    private int falseNegatives = 0;

    // TODO(vcarbune): Should the constructor play the role of the classify method?
    public Classifier() {
    }
    
    /**
     * Classifies a given document using naive bayes rule.
     *  
     * @param entry             The document entry to be classified.
     * @param frequencyMap      The complete frequency map.
     * @param spamClassPrior    The prior probability for the spam class.
     *
     * @return Boolean value if whether the document is spam or not.
     */
    public boolean classifyDocument (DocEntry entry, FrequencyMap frequencyMap,
            double spamClassPrior) {
        // We're not expecting more than two classes (in this order): Ham and Spam

        double logHamSum = 0.0;
        int denominatorHam = frequencyMap.getTotalCounts().get(0) +
                frequencyMap.getVocabularySize();

        double logSpamSum = 0.0;
        int denominatorSpam = frequencyMap.getTotalCounts().get(1) +
                frequencyMap.getVocabularySize();

        for (String word : entry.getWords()) {
            logHamSum   += Math.log((frequencyMap.getWordHamCount(word) + 1) / denominatorHam);
            logSpamSum  += Math.log((frequencyMap.getWordSpamCount(word) + 1) / denominatorSpam);
        }

        logHamSum += Math.log(1 - spamClassPrior);
        logSpamSum += Math.log(spamClassPrior);

        // TODO(vcarbune): Make sure this is the right check.
        return logSpamSum > logHamSum;
    }

    /**
     * Classifies each of the documents in the given document set.
     *
     * @param docSet        The documents to be classified.
     * @param frequencyMap  The complete frequency map.
     * @param spamClassPrior    The prior probability for the spam class.
     */
    public void classify(DocSet docSet, FrequencyMap frequencyMap, double spamClassPrior) {
        // TODO(vcarbune): Guess docEntries shouldn't be public in FrequencyMap so it might change.
        for (DocEntry entry : docSet.docEntries) {
            boolean isSpam = classifyDocument(entry, frequencyMap, spamClassPrior);

            if (isSpam == entry.isSpam()) {
                // Correct identification.
                truePositives += isSpam ? 1 : 0;
                trueNegatives += isSpam ? 0 : 1;
            } else {
                // Wrong identification.
                falsePositives += isSpam ? 1 : 0;
                falseNegatives += isSpam ? 0 : 1;
            }
        }
    }

    public int getTruePositives() {
        return truePositives;
    }

    public int getTrueNegatives() {
        return trueNegatives;
    }

    public int getFalsePositives() {
        return falsePositives;
    }

    public int getFalseNegatives() {
        return falseNegatives;
    }
}